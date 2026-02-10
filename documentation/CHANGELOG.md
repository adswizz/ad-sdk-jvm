[<img src="../img/adswizz_logo.png" alt="AdsWizz" style="width:30%;">](https://www.adswizz.com)
# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.6.5] - 2026-02-10
### Added
- Added support for `java.time.Duration` in the public API
- Added `BasicSpringBootSampleJava` sample project. Fore more details, please visit the [sample projects](JvmSDK/SDK/SampleProjects.md) page.

### Changed
- Changed duration parameters to be either `kotlin.time.Duration` or `java.time.Duration` instead of double/long primitives.
- Changed `AdswizzAdZone` and `AdswizzShimZone` instance creation to Builder pattern to improve **Java** interoperability. For more details, please visit the updated [AdInsertion](JvmSDK/SDK/AdInsertion.md) page.
- Improved **Java** interoperability for the`AdswizzSDK` singleton. For more details, please visit the updated [SDK initialization](JvmSDK/SDK/GetStarted.md) section.

## [0.4.0] - 2025-12-19
### Added
- This is a Beta version, some APIs may change in future releases based on feedback. 
- Brand new AddswizzSDK for JVM applications written in **Kotlin**
- Support for **Java**
