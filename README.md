# SignalLab: Digital Communication Toolkit

SignalLab is a command-line Java project for experimenting with digital data communication concepts. It brings several encoding, decoding, scrambling, modulation, analysis, and waveform-visualization exercises into one place.

> **Project provenance:** This version is adapted from the supplied `Digital_Signal_Generator` project. Core algorithms are retained; the repository has been given a new project identity and clearer supporting documentation. If you submit or publish it, follow your course's collaboration and attribution rules and describe any changes you personally make.

## Capabilities

- Generate random, patterned, or custom binary sequences.
- Inspect bitstreams with a palindrome-finding algorithm.
- Encode/decode NRZ-L, NRZ-I, Manchester, Differential Manchester, and AMI.
- Apply and reverse B8ZS and HDB3 scrambling.
- Convert normalized analog samples using PCM or delta modulation.
- Calculate signal metrics and visualize waveforms using Java Swing.

## Repository layout

```text
SignalLab_Communication_Toolkit/
├── src/
│   ├── Main.java
│   ├── data/
│   ├── encoding/
│   ├── decoding/
│   ├── modulation/
│   ├── scrambling/
│   ├── visualization/
│   └── *Test.java
├── docs/
│   └── PROJECT_STRUCTURE.md
├── SignalLab_Technical_Specification.pdf
└── README.md
```

## Requirements

- JDK 17 or later
- A desktop environment if you want to open the Swing plots

## Compile

Run these commands from the project root.

### Linux / macOS

```bash
mkdir -p bin
javac -d bin $(find src -name '*.java')
```

### Windows PowerShell

```powershell
New-Item -ItemType Directory -Force bin | Out-Null
javac -d bin (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
```

## Run the application

```bash
java -cp bin Main
```

## Run the main verification suite

```bash
java -cp bin ProjectTest
```

Other standalone test runners are available in `src/`. Run them by class name after compiling, for example:

```bash
java -cp bin EncodingDecoderTest
java -cp bin ScramblingTest
```

## Notes

- Enter binary input using only `0` and `1`.
- PCM and delta-modulation sample values are expected to be in the range `[-1.0, 1.0]`.
- The `src/` Java filenames intentionally match their public class names; renaming them in isolation would make the project fail to compile.
- See `docs/PROJECT_STRUCTURE.md` for a short module guide.
