# Before you start

This guide is addressed to developers who want to integrate the AdswizzSDK in their apps. Here is a quick overview. More details will be provided for each scenario in their respective section.

## Thread safety
The AdswizzSDK is not thread safe, so it is expected that all the calls to the SDK are done on the main thread. The AdswizzSDK does all the heavy lifting on other threads, so that the main one remains responsive.

## What is Client-Side Insertion

Client-Side Insertion represents the insertion of ads into the audio/video stream done by the integrator. Your app is responsible for the built-in logic to decide when to start and end an ad break by fetching ads from the ad server and playing them with your app's audio/video capabilities. The SDK will handle all communication during ad fetching with the ad server and performing various event reporting (impressions & quartiles).

## Prerequisites for Client-Side Insertion

To successfully perform Client-Side Insertion you will need to set/provide the following information for your **_AdswizzAdRequest_** object within **_AdRequestConnection_**:
*  adServer = the name of the AdServer to fetch ads from
*  zones = one or more ```AdswizzAdZone``` to identify the zone/zones to retrieve audio/video ads from
*  companionZones = (optional) identifier of the zone from which to retrieve creatives to be displayed by the companion banner
*  (optional) list of custom site variables used for ad selection (e.g referrer)
*  (optional) GDPR consent value
*  (optional) CCPA (U.S. privacy string) consent value
*  (optional) macro context. This contains information that the integrator should provide, and will be used by the SDK for VAST macro expansion.