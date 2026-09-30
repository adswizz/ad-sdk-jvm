[<img src="../img/adswizz_logo.png" alt="AdsWizz" style="width:30%;">](https://www.adswizz.com)
# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.9.2] - 2026-09-30
### Added
- Added new fields to the `AdData` class
  - `title` The ad title field from VAST response.
  - `description` The ad description field from VAST response.
### Changed
- Improved VAST parser flexibility by allowing non-standard syntax and using a best-effort approach for VASTs with illegal characters or VASTs that ends unexpectedly.

## [0.9.1] - 2026-08-20
### Added
- Added empty ads reporting. The SDK will fire `<Error>` trackers automatically when no ads are present in the VAST response.
### Changed
- Changed the analytics behaviour to emmit the `AnalyticsEvent` in sync with the fired tracking triggered by any `AdswizzTrackingEngine` call.


## [0.9.0] - 2026-07-30
### Changed
- Changed the macros behaviour so that all unknown macros supported by the SDK to be expanded with `-1`.

### Fixed
- Fixed the `SDKErrorCode` enum class to be kept during the obfuscation process.

## [0.8.0] - 2026-06-23
### Added
- Added new `AdswizzHttpConfig` class used to pass HTTP configuration options to `AdRequestConnection` and `AdswizzTrackingEngine`. This class can be used to configure timeouts, headers, redirects and cookie policies, for more details please visit the updated [API docs](https://api.sdk.adswizz.com/jvmSDK/index.html) page.
- Added new methods:
  - `AdRequestConnection.setAdswizzHttpConfig(adswizzHttpConfig: AdswizzHttpConfig)` used to pass AdswizzHttpConfig instance to the Ad Request sent to the Ad Server.
  - `AdswizzTrackingEngine.Builder.withHttpConfig(adswizzHttpConfig: AdswizzHttpConfig)` used to pass AdswizzHttpConfig instance to the tracking requests.
- Added java doc for `AdswizzTrackingEngine` and `AdswizzTrackingEngine.Builder`, for more details please visit the updated [API docs](https://api.sdk.adswizz.com/jvmSDK/index.html) page.

### Changed
- Removed the `setTimeout(timeout: Duration)` in `AdRequestConnection` in favor of `setAdswizzHttpConfig(adswizzHttpConfig: AdswizzHttpConfig)`


## [0.7.2] - 2026-05-25
### Fixed
- Fixed the `Advertiser`, `AdParameters`, `StaticResorce` & `ClickTracking` names to be kept during the obfuscation process. 

## [0.7.1] - 2026-05-20
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
