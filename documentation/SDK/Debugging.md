# Debugging the AdswizzSDK

The AdswizzSDK uses an internal logger called **_AdLogger_**. This logger handles various debug messages by either using the default Log system or by utilizing a custom `Logger` interface implemented by the consumer of AdswizzSDK. 

If the **_AdswizzSDK.logger_** property is assigned with an implementation of the **_Logger_** interface, the messages are sent back through the `log` callback.

```kotlin
AdswizzSDK.logger = object : Logger{
    override fun log(type: LogType, tag: String, message: String) {
        // add your custom logs
    }
}
```
```java
AdswizzSDK.setLogger((logType, s, s1) -> {
    // add your custom logs
});
```


