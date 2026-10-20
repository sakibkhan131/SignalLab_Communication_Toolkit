# SignalLab — Project Structure

This repository is a Java learning toolkit for exploring digital communication techniques.

## Source modules

- `src/data/` — bitstream generation and palindrome analysis.
- `src/encoding/` — line-code encoders.
- `src/decoding/` — corresponding decoders.
- `src/modulation/` — PCM and delta modulation components.
- `src/scrambling/` — B8ZS and HDB3 scrambling/descrambling.
- `src/visualization/` — Swing-based signal plots.
- `src/Main.java` — command-line entry point.
- `src/*Test.java` — standalone test runners.

## Why Java source filenames remain unchanged

Java public classes must normally be stored in files with matching names. Renaming those files without also renaming the classes and updating every reference would break compilation. The repository and documentation have been reorganized while preserving the class/file contract.
