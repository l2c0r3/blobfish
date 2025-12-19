<img style="float: right" src="docs/icon.png" alt="Blobfish icon" width="160"/>

# Blobfish

A simple alternative to Stockfish.

## What is this?

**Blobfish** is a small Java chess engine project with the stated goal of being *“a simple alternative to Stockfish.”*

## How to start it

First you have to build an the to start it

### How to build

#### Linux / macOS

```bash
./mvnw clean test
./mvnw clean package
```

#### Windows (PowerShell)

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package
```

## Run

Depending on how the jar is configured (manifest / plugins), one common way is:

```bash
java -jar target/*.jar
```

If that doesn’t work, open the project in your IDE as a Maven project and run the configured main class.

## Implementation Details


### Overview of the implementation


### Class diagrams (SVG)

Generated class diagrams are stored here:

- Folder: `docs/generated-diagrams/svg/`
- Repo view: https://github.com/l2c0r3/blobfish/tree/main/docs/generated-diagrams/svg

#### Embedded diagrams

> Note: GitHub README embeds need exact filenames. If you add/rename diagrams, update the paths below.

<details>
  <summary><strong>Class diagrams</strong> (click to expand)</summary>

  <!-- Replace these filenames with the actual SVG names in docs/generated-diagrams/svg/ -->
  <p>
    <img src="docs/generated-diagrams/svg/class-diagram.svg" alt="Class diagram" />
  </p>

</details>