# Ad Insertion

## Your first ad request

You are now ready for your first ad request. You will need to create an AdswizzAdRequest object and configure it.

**Minimal request parameters** The `AdswizzAdRequest` requires at minimum to set the server url and a set of one ore more `AdswizzAdZone` or an `AdswizzShimZone`.

```kotlin
    data class AdswizzAdZone @JvmOverloads constructor(
    @Json(name = "zoneId")
    val zoneId: String,
    @Json(name = "maxAds")
    val maxAds: Int? = null,
    @Json(name = "maxDuration")
    val maxDuration: Double? = null,
    @Json(name = "zoneAlias")
    val zoneAlias: String? = null
)
```
Each `AdswizzAdZone` object is defined by a mandatory `zoneId` or `zoneAlias`, provided by the PIM/TAM, and optional `maxAds` and `maxDuration` fields. The optional fields are determined by the application logic and are not required.

**Note**
`zoneId` parameter is not mandatory if `zoneAlias` is present. However, they cannot be set simultaneously with `null` or `not null` values, this will lead to a runtime exception.

```kotlin
data class AdswizzShimZone @JvmOverloads constructor(
    @Json(name = "zoneId")
    val zoneId: String? = null,
    @Json(name = "maxDeadAir")
    val maxDeadAir: Long? = null,
    @Json(name = "zoneAlias")
    val zoneAlias: String? = null
)
```
Each `AdswizzShimZone` object is defined by a mandatory `zoneId`/`zoneAlias` and an optional `maxDeadAir` field, where `zoneId` or `zoneAlias` is provided by the PIM.
The fields `zoneId` or `zoneAlias` checked in the same manner as AdswizzAdZone. The `maxDeadAir` parameter represents the minimal threshold time necessary to fill the remaining duration with shims ads.

The minimal request looks like the following: one or more zones and a server url 
```kotlin
	val adswizzAdRequest = AdswizzAdRequest.Builder() //Build the Ad Request with the needed parameters
            .withServer("SERVER_PROVIDED_BY_PIM")
            .withZones(listOf(AdswizzAdZone("ZONEID_1_PROVIDED_BY_PIM"), ... AdswizzAdZone("ZONEID_n_PROVIDED_BY_PIM")))
            .build()
```

```java
    AdswizzAdRequest adswizzAdRequest = null;
    try {
        adswizzAdRequest = new AdswizzAdRequest.Builder()
            .withServer("SERVER_PROVIDED_BY_PIM")
            .withZones(List.of(new AdswizzAdZone("ZONEID_1_PROVIDED_BY_PIM"), new AdswizzAdZone("ZONEID_n_PROVIDED_BY_PIM")))
            .build();
    } catch (SDKError e) {
    }
```

The ad server and the zoneId(s) will be provided to you by an AdsWizz PIM.

> **Note**
> Ensure the provided `AdswizzAdZone` array is ordered and unique, as this directly impacts the sequence of ads returned by the Ad Server.


**Add shims zone to AdswizzAdRequest**
Only one shim zone can be set in `AdswizzAdRequest`.

```kotlin
	val adswizzAdRequest = AdswizzAdRequest.Builder() //Build the Ad Request with the needed parameters
            .withServer("SERVER_PROVIDED_BY_PIM")
            .withShimZone(AdswizzShimZone("SHIM_ZONE_ID"))
            .build()
```

```java
    AdswizzAdRequest adswizzAdRequest = null;
    try {
        adswizzAdRequest = new AdswizzAdRequest.Builder()
            .withServer("SERVER_PROVIDED_BY_PIM")
            .withShimZone(new AdswizzShimZone("SHIM_ZONE_ID"))
            .build();
    } catch (SDKError e) {
    }
```

**Custom request parameters**

You may use custom parameters (key-value pairs) in your requests, in order to send various information like the publisher-specific ID, first-party targeting data etc.
Here is an example:

```kotlin
	AdswizzAdRequest.Builder()
		.withCustomParameter("key1=URLEncodedValue1&key2=URLEncodedValue2")
		...
```

**Duration request parameters**

An optional parameter is duration, which sets the maximum total duration of ads to be selected, in seconds. It can be used to fine tune the ad delivery and request a specific total duration.
Duration is set in milliseconds. Here is an example that sets max duration to 90 seconds

```kotlin
	AdswizzAdRequest.Builder()
		...	
		.withDuration(90000) // milliseconds
		...
```

**AdRequestConnection**

At this point you need to create an AdRequestConnection object and call requestAds with the ad request object.

```kotlin            
    val adRequestConnection = AdRequestConnection(adRequest) // Create the AdRequestConnection using the above adRequest

    val adResponseItems = adRequestConnection.requestAds()
    adResponseItems.forEach { adResponseItem ->
        // handle
        adResponseItem.adData
        adResponseItem.payload
    }
```

```java
    AdRequestConnection requestConnection = new AdRequestConnection(req);
    List<AdResponseItem> adResponseItems;
    try {
        adResponseItems = requestConnection.requestAds();
    } catch (Throwable e) {
    }

    adResponseItems.forEach(adResponseItem -> {
        // handle
        adResponseItem.getAdData();
        adResponseItem.getPayload();
    });
```

As a result of this call, the SDK will throw an error if the call was a failure or a AdResponseItem list if the result was a success.

## Working with AdResponse List

If the request to the Adswizz Ad server was a success, the SDK will return a AdResponseItem list, that you will own.
Work in Progress
