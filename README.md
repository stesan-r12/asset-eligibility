# Asset Eligibility

Java API for defining and evaluating asset eligibility criteria.

## Requirements

- Java 21
- No Maven installation needed: the Maven Wrapper is included

## Build and run the tests

```bash
./mvnw clean test       # Linux / macOS / Git Bash
mvnw.cmd clean test     # Windows cmd / PowerShell
```

`./mvnw clean package` also builds the library jar in `target/`.
The only dependency is JUnit 5, used for the tests.

## Design

The main design decisions are described in [DESIGN.md](DESIGN.md).
