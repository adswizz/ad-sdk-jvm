# AdswizzTrackingEngine

## AdswizzTrackingEngine class

The `AdswizzTrackingEngine` is the core of Server Side SDK business logic, the API and the main interface of the SDK.
The interface consists of a simple interface with only 5 functions **_started_**, **_error_**, **_progress_**, **_onEvent_**, **_ended_**.

```kotlin
interface AdswizzTrackingEngine {
    @Throws(
        AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class
    )
    fun started(payload: ByteArray, elapsedTime: kotlin.time.Duration?, macroContext: MacroContext?): ByteArray

    @Throws(
        AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class
    )
    fun started(payload: ByteArray, elapsedTime: java.time.Duration?, macroContext: MacroContext?): ByteArray

    @Throws(
        AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class
    )
    fun error(payload: ByteArray, elapsedTime: kotlin.time.Duration?, macroContext: MacroContext?): ByteArray

    @Throws(
        AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class
    )
    fun error(payload: ByteArray, elapsedTime: java.time.Duration?, macroContext: MacroContext?): ByteArray

    @Throws(
        AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class
    )
    fun progress(payload: ByteArray, elapsedTime: kotlin.time.Duration, macroContext: MacroContext?): ByteArray

    @Throws(
        AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class
    )
    fun progress(payload: ByteArray, elapsedTime: java.time.Duration, macroContext: MacroContext?): ByteArray

    @Throws(
        AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class
    )
    fun onEvent(
        payload: ByteArray,
        elapsedTime: kotlin.time.Duration?,
        event: AdEventType,
        macroContext: MacroContext?
    ): ByteArray

    @Throws(
        AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class
    )
    fun onEvent(
        payload: ByteArray,
        elapsedTime: java.time.Duration?,
        event: AdEventType,
        macroContext: MacroContext?
    ): ByteArray

    @Throws(
        AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class
    )
    fun ended(
        payload: ByteArray,
        elapsedTime: kotlin.time.Duration?,
        reason: AdReasonType,
        macroContext: MacroContext?
    ): ByteArray

    @Throws(
        AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class
    )
    fun ended(
        payload: ByteArray,
        elapsedTime: java.time.Duration?,
        reason: AdReasonType,
        macroContext: MacroContext?
    ): ByteArray

}
```

The byte array returned by each function contain states that are used to preserve the state between consecutive calls. Each byte array returned by a function call
should be used for the next API call and so on, otherwise the VAST events could be fired multiple times.

## AdswizzTrackingEngine.Builder class

In order to get an implementation the companion builder must be used. For the initial version only one parameter is available that sets up
how the Engine works. 

```kotlin
class Builder {
    private var currentTrackingPolicy: TrackingPolicy = TrackingPolicy.InferredTrackingPolicy

    fun withAutomaticTrackingDecisions(trackingPolicy: TrackingPolicy): Builder = apply {
        this.currentTrackingPolicy = trackingPolicy
    }

    fun build(): AdswizzTrackingEngine {
        return AdswizzTrackingEngineImpl(currentTrackingPolicy)
    }
}
```
The default `AdswizzTrackingEngine` instance is defined as follows:

```kotlin
companion object {
    @JvmStatic
    @get:JvmName("getDefault")
    val DEFAULT = Builder().build()
}
```
And it can be used in your App implementation:
```kotlin
val trackingEngine = AdswizzTrackingEngine.DEFAULT
trackingEngine.started(byteArray, ...)
```

```java
AdswizzTrackingEngine trackingEngine = AdswizzTrackingEngine.getDefault();
trackingEngine.started(byteArray, ...);
```

The default version of the engine is accessed as a static object in the interface. The default implementation of the engine require strict
API usage in a definite order. After initial request to ad delivery, the track engine is used to fire quartiles and other internal logic.
In the default cases there should be a strict order: `started` method should be called first, followed by `progress` method with playhead updates and `onEvent` if is the case
and should end the playback of the current ad with `ended` or `error` API call. The SDK will fire quartiles based on these calls without any specific assumption.

In case `withAutomaticTrackingDecisions` builder method is used with a true value, the default logic is overwritten and a specific engine which makes assumptions 
will be used. In this case, calling `progress` without calling `started` first will internally decide  to fire the impressions events  as well as other events that are needed for the current playhead position 
like it should if the `started` function was called. So it fills the missing api calls based on some internal assumptions.


## Payload byte array

The payload contains state required by the SDK to operate and consist of impressions url, tracking url, error urls a playing state and a macrocontext initially set in the ad request.

> **Note**
> Each call to the `AdswizzTackingEngine` methods with macroContext parameter will update the previous values with the current ones.

## AdswizzTrackingEngine Usage

```kotlin
val trackingEngine = AdswizzTrackingEngine.DEFAULT
/**
 * the macro context state will remain empty, no macro expansion will be done after the started call
 */
payload = trackingEngine.started(payload = payload, elapsedTime = 0.milliseconds, macroContext = null) // no updates for the existing macrContext

// ...
val additionalContextWithPlayerState = MacroContext.Builder()
    .withPlayerState(listOf(PlayerState.MUTED, PlayerState.BACKGROUND))
    .build()

/**
 * the macro context state will contain [PLAYERSTATE] values after progress call
 * all the encountered macros with [PLAYERSTATE] will be expanded
 */
payload = trackingEngine.progress(payload = payload, elapsedTime = 5000.milliseconds, macroContext = additionalContextWithPlayerState)


// ...
val additionalContextWithAsset = MacroContext.Builder()
    .withPlayerState(listOf(PlayerState.FULLSCREEN, PlayerState.FOREGROUND))
    .withAssetUri("yourAssetUri.com")
    .build()

/**
 * the macro context state will have:
 * the [PLAYERSTATE] value : PlayerState.FULLSCREEN, PlayerState.FOREGROUND
 * the [ASSETURI] value : yourAssetUri.com
 * all the encountered macros with [PLAYERSTATE] and [ASSETURI] will be expanded with specified values after calling ended method.
 */
payload = trackingEngine.ended(payload = payload, elapsedTime = 15000.milliseconds, reason = AdReasonType.AD_COMPLETED, macroContext = additionalContextWithAsset)

```

```java
AdswizzTrackingEngine trackingEngine = AdswizzTrackingEngine.getDefault();
/**
 * the macro context state will remain empty, no macro expansion will be done after the started call
 */
payload = trackingEngine.started(payload, Duration.ofMillis(0), null); // no updates for the existing macrContext

// ...
MacroContext additionalContextWithPlayerState = new MacroContext.Builder()
        .withPlayerState(List.of(PlayerState.MUTED, PlayerState.BACKGROUND))
        .build();

/**
 * the macro context state will contain [PLAYERSTATE] values after progress call
 * all the encountered macros with [PLAYERSTATE] will be expanded
 */
payload = trackingEngine.progress(payload, Duration.ofMillis(5000), additionalContextWithPlayerState);


// ...
MacroContext additionalContextWithAsset = new MacroContext.Builder()
        .withPlayerState(List.of(PlayerState.FULLSCREEN, PlayerState.FOREGROUND))
        .withAssetUri("yourAssetUri.com")
        .build();

/**
 * the macro context state will have:
 * the [PLAYERSTATE] value : PlayerState.FULLSCREEN, PlayerState.FOREGROUND
 * the [ASSETURI] value : yourAssetUri.com
 * all the encountered macros with [PLAYERSTATE] and [ASSETURI] will be expanded with specified values after calling ended method.
 */
payload = trackingEngine.ended(payload, Duration.ofMillis(15000), AdReasonType.AD_COMPLETED, additionalContextWithAsset);

```
