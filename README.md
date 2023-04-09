# erss-hwk4-fq18-jl1188

# Concurrency in Stock Exchange Application

This project has two branches: [main](https://gitlab.oit.duke.edu/fq18/erss-hwk4-fq18-jl1188/-/tree/main) and [version](https://gitlab.oit.duke.edu/fq18/erss-hwk4-fq18-jl1188/-/tree/version).

## Concurrency handling

- The earlier code in the `main` branch uses locks for different tables to handle concurrency. (commit a17e140a23d0b2d565c00e54319307c4ddab60d6)
- The `version` branch uses optimistic locking to handle concurrency.

## Testing

- Our testing infrastructure is place in directory: src/test/java
