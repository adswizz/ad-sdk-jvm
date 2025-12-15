# AdswizzSDK analytics

The AdswizzSDK collects and exposes information on SDK events that occur during its lifecycle. It was built to log different event types (informative and errors) that cover both integration robustness and ad lifecycle management in the ad insertion scenario.

To enable you to complement your specific app analytics system (e.g. Firebase, Mixpanel etc) with AdswizzSDK internal events, the analytics framework provides you with the ability to receive all the events from the SDK and forward them to your own ingestion pipelines by implementing our AnalyticsConnector interface.
To learn more about the types of events you can listen and use, please reach out to your Integration Manager at Adswizz.

To register and receive the analytics events, you have to do two things:

1. Implement our AnalyticsConnector interface
```kotlin
class MyAnalyticsConnector: AnalyticsConnector {
    
    override val id: String = "my-adswizz-connector" // The id used by SDK to identify the added connector
    
    override fun onLog(analyticsEvent: AnalyticsEvent) {
        // every time we log something you will also get the callback here and can
        // do whatever you want with the event
    }
}
```
```java
AnalyticsConnector connector = new AnalyticsConnector() {
    @Override
    public @NotNull String getId() {
        return "my-adswizz-connector"; // The id used by SDK to identify the added connector
    }

    @Override
    public void onLog(@NotNull AnalyticsEvent analyticsEvent) {
        // every time we log something you will also get the callback here and can
        // do whatever you want with the event
    }
};
```
**NOTE**
AdswizzSDK forwards the analytics events for each added connector, so the `id` value of your custom connector should be provided by your Integration Manager during the integration process.

2. Add yourself as a listener, using the following lines of code:
```kotlin
myAnalyticsConnector = MyAnalyticsConnector()
AdswizzSDK.analytics?.add(myAnalyticsConnector)
```

```java
AnalyticsCollector analyticsCollector = AdswizzSDK.INSTANCE.getAnalytics();
if (analyticsCollector != null) {
    analyticsCollector.add(connector);
}
```

---
**NOTE**
The AdswizzSDK doesn't replay already emitted analytics events to the newly added connectors, so in order to catch all events, you should add the analytics connector right after initialisation is completed, as follows:
```kotlin
AdswizzSDK.initialize("YOUR_INSTALLATION_ID"){ 
    AdswizzSDK.analytics?.add(connector)
}
```

```java
AdswizzSDK.INSTANCE.initialize("YOUR_INSTALLATION_ID", () -> {
    AnalyticsCollector analyticsCollector = AdswizzSDK.INSTANCE.getAnalytics();
    if (analyticsCollector != null) {
        analyticsCollector.add(connector);
    }
});
```
When a connector is no longer needed, it can be removed as follows:
```kotlin
AdswizzSDK.analytics?.remove(connector)
```
```java
AnalyticsCollector analyticsCollector = AdswizzSDK.INSTANCE.getAnalytics();
if (analyticsCollector != null) {
    analyticsCollector.remove(connector);
}
```