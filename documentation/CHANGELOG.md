[<img src="../img/adswizz_logo.png" alt="AdsWizz" style="width:30%;">](https://www.adswizz.com)
# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.7.1] - 2026-05-15
### Added
- Added new methods in MacroContext.Builder:
  - `withErrorCode(errorCode: Int)` used for replacing the [ERRORCODE] macro. This is an overloaded version for `withErrorCode(errorCode: VASTErrorCode)`.
  - `withCustomMacroValues(customMacroValues: Map<String, String>)` used to replace any non-VAST macro with the given key. For more details, please visit the [Macros](JvmSDK/SDK/VastMacros.md) page.
- Added `advertiser`, `selectedCreativeId`, `companions`  and `creativeExtensions` fields to the AdData class.
- Added a new mechanism to support generic XML objects defined within `CreativeExtension` and `Extension` VAST tags. The parsed XML objects are exposed through new `XmlNode` data class, accessible from CreativeExtension and Extension classes.
- Added a new method fun `withQueryParameters(parameters: Map<String, String>)` to the AdswizzAdRequest.Builder. This method accepts unencoded key value pairs used to append additional parameters to AdswizzAdRequest.
- Added new property `minExtraDuration` in AdswizzShimZone data class. This specifies the minimum duration that the ad server is required to fill for the shim zone.

### Changed
- Removed the `maxDeadAir` property from AdswizzShimZone.

## [0.7.0] - 2026-03-24
### Added
- Added support for CONTENTID and CONTENTURI macros. For more details, please visit the [Macros](JvmSDK/SDK/VastMacros.md) page.
### Changed
- Changed `MacroContext.withMediaPlayHead` and `MacroContext.withContentPlayHead` methods to accept either `kotlin.time.Duration` or `java.time.Duration`.

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
