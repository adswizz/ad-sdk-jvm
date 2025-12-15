# AdswizzSDK consents

The `AdswizzPrivacyConsent` class is used to encapsulate all consents used by AdswizzSDK. Once you have created the `AdswizzPrivacyConsent` instance, it should be passed to an `AdswizzAdRequest.Builder` as follows:
```kotlin
val consent = AdswizzPrivacyConsent.Builder()
    //consents
    .build()

AdswizzAdRequest.Builder()
    .withConsent(consent)
    // other 
    .build()
```

```java
AdswizzPrivacyConsent consent = new AdswizzPrivacyConsent.Builder()
        //consents
        .build();

try {
    AdswizzAdRequest request = new AdswizzAdRequest.Builder()
            .withConsent(consent)
            // other
            .build();
    } catch (SDKError e) {
    }

```

## GDPR consent

Adswizz services are GDPR-compliant. The **_AdswizzSDK_** will decorate urls that connect to Adswizz services accordingly, to reflect the desired GDPR user consent. To modify the GDPR consent, you need to configure it like this:

```kotlin
val consent = AdswizzPrivacyConsent.Builder()
    .withGDPR(GDPRConsent.GRANTED)
    .build()
```
```java
AdswizzPrivacyConsent consent = new AdswizzPrivacyConsent.Builder()
    .withGDPR(GDPRConsent.GRANTED)
    .build();
```

If not configured, the default value for GDPR consent in the **_AdswizzSDK_** is **_NOT_APPLICABLE_**. This means that the user is not subject to the GDPR. Other possible values are:
* **_NOT_ASKED_**: the user has not been asked yet about GDPR consent
* **_GRANTED_**: the user has granted access
* **_DENIED_**: the user has denied access

## CCPA config

With the introduction of CCPA in US, the **_AdswizzSDK_** enables you to forward the user consent accordingly.

```kotlin
val consent = AdswizzPrivacyConsent.Builder()
    .withCCPA(CCPAConfig(explicitNotice = CCPAConsent.YES, optOut = CCPAConsent.NO, lspa = CCPAConsent.NOT_APPLICABLE))
    .build()
```
```java
AdswizzPrivacyConsent consent = new AdswizzPrivacyConsent.Builder()
    .withCCPA(new CCPAConfig(CCPAConsent.YES, CCPAConsent.NO, CCPAConsent.NOT_APPLICABLE))
    .build();
```

The first value represents the explicit notice for collecting consent. The next value is the opt-out of the sale of personal information. The last one is the limitation of scope to the Limited Service Provider Agreement. For all tree, possible values are: **_NOT_APPLICABLE_**, **_YES_**, **_NO_**. The default value for all of them is **_NOT_APPLICABLE_**.

### GPP

With the introduction of GPP standard from IAB, **_AdswizzSDK_** gives you the possibility to forward the gpp consent string accordingly.

```kotlin
val consent = AdswizzPrivacyConsent.Builder()
    .withGPP("My GPP Consent String")
    .build()
```
```java
AdswizzPrivacyConsent consent = new AdswizzPrivacyConsent.Builder()
    .withGPP("My GPP Consent String")
    .build();
```

The default value for **gppConsent** is null, which means this parameter won't be used by **_AdswizzSDK_**. The usage of the GPP consent string is similar to the TCF V2 consent string.

### TCFv2

With the introduction of TCFv2 standard from IAB, **_AdswizzSDK_** gives you the possibility to forward the TCFv2 consent string accordingly. The default value is null.

```kotlin
val consent = AdswizzPrivacyConsent.Builder()
    .withTCFV2("My TCFV2 Consent String")
    .build()
```
```java
AdswizzPrivacyConsent consent = new AdswizzPrivacyConsent.Builder()
    .withTCFV2("My TCFV2 Consent String")
    .build();
```

### GPC

With [Global Privacy Control](https://w3c.github.io/gpc/) value set, the **_AdswizzSDK_** will add `Sec-GPC` header to the Ad Request. The default value is false.

```kotlin
val consent = AdswizzPrivacyConsent.Builder()
    .withGPC(true)
    .build()
```
```java
AdswizzPrivacyConsent consent = new AdswizzPrivacyConsent.Builder()
    .withGPC(true)
    .build();
```

