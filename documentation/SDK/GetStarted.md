# Get started

The AdswizzSDK facilitates the ad lifecycle management, right from your application. In a client-side insertion scenario, it retrieves the ad with its associated assets, and handles the reporting operations.<br>

## Prerequisites

* Java 11 (or later)
* Gradle or Maven build tools

## Changelog

Please check [here](/JvmSDK/CHANGELOG.md) for the latest AdswizzSDK version and all the new changes & fixes it comes with.

## Dependencies

```groovy
kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${kotlin_version}"
kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core${kotlin_coroutines_version}"
xmlParser = "net.sf.kxml:kxml2:${xml_parser_version}"
moshi = "com.squareup.moshi:moshi:${moshi_version}"
moshiAdapters = "com.squareup.moshi:moshi-adapters:${moshi_version}"
encoder = "org.owasp.encoder:encoder:${encoder_version}"
```
We have tested the above dependencies using the following version numbers:<br>

```groovy
kotlin_version = "1.9.10"
kotlin_coroutines_version = "1.7.3"
xml_parser_version = "2.3.0"
moshi_version = "1.14.0"
encoder_version = "1.4.0"

```

---
**NOTE**
If you are excluding any tested dependency version and using another version (that doesn't ensure backward/forward compatibility with the version we have tested) you may encounter unexpected behavior.<br>
---  

## Adding the SDK to your Project

You can easily integrate the AdswizzSDK into your project. There are 2 steps to this integration:

1. Inside your project level build.gradle add the following block inside the repositories tag:

```groovy
maven {
    url "https://raw.githubusercontent.com/adswizz/ad-sdk-jvm/main/releases"
}
```

The final code should look something like this:

```groovy
repositories {
    mavenCentral()
    google()

    maven {
        url "https://raw.githubusercontent.com/adswizz/ad-sdk-jvm/main/releases"
    }
    // .... other repositories declared
}
```

2. Then, inside your dependencies block add the following lines :

```groovy
dependencies {
    
    implementation("com.adswizz:sdk-jvm:$sdkVersion")
    //.... other dependencies
}
```
In the code snippet above, replace <strong>SdkVersion</strong> with the version of SDK provided by Adswizz (i.e. 0.6.5 - the latest version). Example:

```groovy
implementation 'com.adswizz:sdk-jvm:0.7.1'

```

## SDK initialization and cleanup

You need to add the installationId, provided by an AdsWizz engineer or PIM, to your app. It should look like this:
```kotlin
AdswizzSDK.initialize("ADD_YOUR_INSTALLATION_ID_HERE"){ 
    // your logic after SDK is initialised 
}
```

```java
AdswizzSDK.initialize("ADD_YOUR_INSTALLATION_ID_HERE", () -> {
    System.out.println("SDK init completed");
    // your logic after SDK is initialised 
});
```

For cleanup, you should use:

```kotlin
AdswizzSDK.cleanup()
```

```java
AdswizzSDK.cleanup();
```
When to call `cleanup`?
- when you no longer need the SDK (E.g.: ad service lifecycle gets cleared/destroyed, business logic decisions like subscription change). This would also clear some class instances kept by the SDK.
- if you want to reinitialise the AdswizzSDK with different installation id.
- if you want to stop receiving analytics events. This would also clear all the `AnalyticsConnectors` added to the SDK.
