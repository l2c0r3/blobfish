<img align="right" src="docs/icon.png" alt="Blobfish icon" width="160"/>

# Blobfish

A simple alternative to Stockfish.

## What is this?

**Blobfish** is a small Java chess engine with a CLI on top of it. 

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `blobfish-1.0.0-SNAPSHOT.jar` file in the `target/` directory.

The application is now runnable using

```bash
java -jar target/blobfish-1.0.0-SNAPSHOT.jar
```


## Overview of the implementation

<img src="docs/generated-diagrams/overview.svg" alt="Overview icon" width="500"/>

Generated class diagrams are stored in  `docs/generated-diagrams/`. A class diagram is available for each package.
