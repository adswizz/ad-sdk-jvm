# Vast Macros

The AdsWizz SDK fully supports the following list of macros:
- TIMESTAMP
- CACHEBUSTING
- CONTENTPLAYHEAD
- MEDIAPLAYHEAD
- BREAKPOSITION
- BLOCKEDADCATEGORIES
- ADCOUNT
- TRANSACTIONID
- ADTYPE
- IFA
- IFATYPE
- CLIENTUA
- DEVICEUA
- SERVERSIDE
- APPBUNDLE
- VASTVERSIONS
- PLAYERCAPABILITIES
- PLAYERSTATE
- ADPLAYHEAD
- ASSETURI
- ADSERVINGID
- LIMITADTRACKING <br>
and partially supports:
- ERRORCODE - only some of the error codes are currently covered

For more information about the macros, consult [VAST_4.2_final_june26.pdf](https://iabtechlab.com/wp-content/uploads/2019/06/VAST_4.2_final_june26.pdf).

## Macrocontext class
The Macrocontext class is used by SDK to expand Macrocontext values in all urls used by SDK to be fired in different VAST events.

```kotlin
	class MacroContext{
	...
	
	
	class Builder {
		
		fun withTimeStamp(timestamp: Date?)

        fun withCacheBusting(cacheBusting: Int?)

        fun withContentPlayHead(contentPlayHead: Double?)

        fun withMediaPlayHead(mediaPlayHead: Double?) 

        fun withBreakPosition(breakPosition: BreakPosition?)

        fun withBlockedAdCategories(blockedAdCategories: List<String>?) 

        fun withAdCount(adCount: Int?)

        fun withTransactionId(transactionId: String?)

        fun withAdAdType(adType: Ad.AdType?)
    
        fun withIfa(ifa: String?) 

        fun withIfaType(ifaType: String?) 

        fun withClientUserAgent(clientUserAgent: String?) 

        fun withDeviceUserAgent(deviceUserAgent: String?)

        fun withServerSide(serverSide: ServerSide?)

        fun withAppBundle(appBundle: String?) 

        fun withStoreId(storeId: String?)
        
        fun withVastVersions(vastVersions: List<VASTVersion>?)

        fun withPlayerCapabilities(playerCapabilities: List<PlayerCapabilities>?)

        fun withPlayerState(playerState: List<PlayerState>?)

        fun withAdPlayHead(adPlayHead: Double?)

        fun withAssetUri(assetUri: String?)

        fun withAdServingId(adServingId: String?)
        
        fun withErrorCode(errorCode: VASTErrorCode?)

        fun withLimitAdTrackingEnabled(limitAdTrackingEnabled: Boolean?)
		
	}

}
```