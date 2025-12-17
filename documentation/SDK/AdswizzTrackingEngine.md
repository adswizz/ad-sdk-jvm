# AdswizzTrackingEngine

The `AdswizzTrackingEngine` is the core of Server Side SDK business logic, the API and the main interface of the SDK.
The interface consist of a simple interface with only 5 functions **_started_**, **_progress_**, **_onEvent_**, **_started_** , **_ended_**, **_error_**

```kotlin
	@Throws(AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class)
    fun started(payload: ByteArray, elapsedTime: Double, macroContext: MacroContext?): ByteArray

    @Throws(AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class)
    fun error(payload: ByteArray, elapsedTime: Double, macroContext: MacroContext?): ByteArray

    @Throws(AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class)
    fun progress(payload: ByteArray, elapsedTime: Double, macroContext: MacroContext?): ByteArray


    @Throws(AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class)
    fun onEvent(payload: ByteArray, elapsedTime: Double, event: AdEventType, macroContext: MacroContext?): ByteArray

    @Throws(AdswizzTrackingException.InvalidPayload::class,
        AdswizzTrackingException.InvalidArgument::class,
        AdswizzTrackingException.InternalError::class,
        AdswizzTrackingException.InvalidElapsedTime::class)
    fun ended(payload: ByteArray, elapsedTime: Double, reason: AdReasonType, macroContext: MacroContext?): ByteArray
```

In order to get an implementation the companion builder must be used. For the initial version only one parameter is available that sets up
how the Engine works. 

```kotlin
   class Builder {
        private var enabled = false

        fun withAutomaticTrackingDecisions(enableTracking: Boolean): Builder 

        fun build(): AdswizzTrackingEngine 
    }
    
```    

There is a default version of the engine which is accessed as a companion object in the interface. The default implementation of the engine require strict
API usage in a definite order. After initial request to ad delivery, the track engine is used to fire quartiles and other internal logic.
In the default cases there should be a strict order: `started` method should be called first, followed by `progress` method with playhead updates and `onEvent` if is the case
and should end the playback of the current ad with `ended` or `error` API call. The SDK will fire quartiles based on this calls without any specific assumption.

In case `withAutomaticTrackingDecisions` builder method is used with a tru value, the default logis is overwrited and a specific engine which makes assumptions 
will be used. In this case, calling `progress` without calling `started` first will internally decide  to fire the impressions events  as well as other events that are needed for the current playhead position 
like it should if the `started` function was called. So it fills the missing api calls based on some internal assupmtions.

```kotlin
 companion object {
        val DEFAULT = Builder().build()
    }
    
```