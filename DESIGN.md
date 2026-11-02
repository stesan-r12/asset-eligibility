# Design decisions

## Project structure

```
src/main/java/com/example/eligibility/
├── Filters.java          single entry point: static factory methods to build filters
├── filter/               AssetFilter interface and its implementations (one record per condition),
│                         plus Validation for constructor argument checks
├── operator/             ordering operators (>, >=, <, <=) used by ordering filters:
│                         ComparisonOperator interface and the StandardComparisonOperator enum
└── converter/            Converter<T> interface and StringToNumericConverter
src/test/java/...         same packages: one test class per component, plus two flow tests
```

## Which operators were implemented and why

| Kind                        | Operators |
|-----------------------------|---|
| Equality (case-insensitive) | `=` |
| Numeric comparisons         | `>`, `>=`, `<`, `<=` |
| Logical                     | `AND`, `OR`, `NOT` |

Also provided, as required: `alwaysTrue()`, `alwaysFalse()` and `exists(p)`.

Why this subset:

- **These are the operators the requirements ask for**: equality, the four numeric comparisons
  (battery level, temperature, ...) and AND / OR / NOT.
- **Together they can express the operators left out**: `!=` is `not(eq(...))`, `BETWEEN` is
  `and(compare(p, GREATER_THAN_OR_EQUAL, a), compare(p, LESS_THAN_OR_EQUAL, b))`, `IN` is
  `or(eq(...), eq(...))`.

Notes:

- Property names are case-sensitive; equality is case-insensitive (`equalsIgnoreCase`). For this reason
  `=` has its own filters (`PropertyEqualsValueFilter`, `PropertyEqualsPropertyFilter`) instead of being a
  `StandardComparisonOperator`: it compares strings, not values ordered by `compareTo`.
- An empty AND is `true`, an empty OR is `false`.

## How filters are represented and composed

```java
AssetFilter filter = and(
        eq("zone", "Lombardy-Plant-A"),
        eq("isOnline", "true"),
        compare("batteryLevel", GREATER_THAN_OR_EQUAL, BigDecimal.valueOf(20)));

filter.matches(sensor);   // true with batteryLevel 85, false with 10
```

- **One abstraction**, `AssetFilter`, with one method: `boolean matches(Map<String, String> asset)`.
- **Every filter is an immutable `record`**, validated in its constructor: built once, reused on many
  assets. Being records, filters can also be analysed or translated (e.g. to SQL) by client code with
  Java 21 record patterns, without adding a Visitor to the library.
- **Composite pattern:** `AndFilter`, `OrFilter` and `NotFilter` contain other `AssetFilter`s, so filters
  nest to any depth.
- **`Filters` is the single entry point** for building filters with a readable syntax.

### Converters

Asset values are always strings, but ordering comparisons need typed values. The `Converter<T>` interface
keeps this conversion out of the filters:

```java
public interface Converter<T> {
    Optional<T> convert(String rawValue);                  // empty if missing or not valid
    default String format(T value) { return value.toString(); }   // used by toString()
}
```

- Ordering filters (`compare`, `compareProperties`) take a `Converter<T>` and compare the converted
  values with `compareTo`; they do not know how the string is parsed.
- `StringToNumericConverter` is the default implementation: it converts to `BigDecimal` and returns empty
  for values that are not numbers. The `compare(p, operator, BigDecimal)` overloads use it implicitly.
- Returning `Optional` instead of throwing is what makes an invalid value simply give `false` (see below).

## How missing properties and invalid comparison values are handled

One rule: **imperfect data makes a condition false; programming errors throw.**

A property is **missing** when its key is absent or mapped to `null`: both cases are treated the same,
so `exists(p)` is false and every comparison on `p` is false.

| Situation | Result |
|---|---|
| Missing property in an equality or ordering comparison | `false` |
| Both properties missing in a property-to-property comparison | `false` (missing ≠ missing) |
| Value that cannot be converted for an ordering comparison (e.g. `"abc"`, `" 85 "`, `"1,5"`) | `false` |
| Empty string | present (`EXISTS` is true), but not a number |
| `null` asset passed to `matches` | `NullPointerException` |
| Invalid construction (null or blank property name, null operator or value) | exception at construction time |

Note: `NOT` turns a missing value into a match (`not(eq("zone", "A"))` is true without `zone`). To
exclude such assets use `and(exists("zone"), not(eq("zone", "A")))`. 

## How to extend the design with a new operator

**A new ordering operator** (e.g. `!=`): implement `ComparisonOperator`. No filter changes.

```java
ComparisonOperator notEqual = new ComparisonOperator() {
    public boolean test(int comparisonResult) { return comparisonResult != 0; }
    public String getSymbol() { return "!="; }
};
AssetFilter filter = compare("batteryLevel", notEqual, new BigDecimal("100"));   // batteryLevel != 100
```

The built-in operators (`StandardComparisonOperator` enum) implement the same interface, so built-in and
custom operators work side by side.

**A new kind of condition** (e.g. `IN`, regex): add a record implementing `AssetFilter` (and optionally a
factory method in `Filters`); it composes with AND / OR / NOT immediately.

**A new value type** (e.g. dates): implement `Converter<T>` (see *Converters*) and pass
it to `compare(p, operator, value, converter)`; the same filters and operators are reused.

## Tests

Run all tests with `./mvnw clean test` (or `mvnw.cmd clean test` on Windows); a single class with
`./mvnw test -Dtest=FilterCompositionTest`.

**One JUnit class per component**, covering, where applicable, match, no match, missing property,
invalid value and `toString()`:

- every filter, in `filter/` (`PropertyEqualsValueFilterTest`, `PropertyComparedToValueFilterTest`,
  `AndFilterTest`, ...);
- the operators (`StandardComparisonOperatorTest`) and the converter (`StringToNumericConverterTest`);
- argument validation (`ValidationTest`).

**Two flow tests** show how the pieces work together:

- `FilterCompositionTest`: builds filters through `Filters` on the case-study sensor, nests AND / OR / NOT,
  shows the guarded `NOT` pattern and the `toString()` of a composed filter.
- `NullAssetTest`: parameterized over every filter type, checks that a `null` asset always throws, even
  for filters that do not read the asset (TRUE, FALSE, empty AND / OR).
