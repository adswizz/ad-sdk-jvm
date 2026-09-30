# AdResponseItem class

This class is used as a result in `requestAds` function of the `AdRequestConnection` class. The result is a list of `AdReponseItem` which contains the first payload and an `AdData`.
This payload [a byte array] is used than in consequent API calls.

```kotlin
/**
 *  AdReponseItem is an object associated with an ad returned by the requestAds function. 
 */
data class AdResponseItem(val payload: ByteArray, val adData: AdData)

/**
 *  AdData object represents an ad item with minimal AdData information.
 */
data class AdData(
    val id: String,
    val title: String,
    val description: String,
    val duration: kotlin.time.Duration,
    val mediaFiles: List<MediaFile>,
    val advertiser: Advertiser?,
    val companions: List<CompanionVast>,
    val selectedCreativeId: String? = null,
    val creativeExtensions: List<CreativeExtension>
) {

    fun getJavaDuration(): java.time.Duration {
        return duration.toJavaDuration()
    }
}
```