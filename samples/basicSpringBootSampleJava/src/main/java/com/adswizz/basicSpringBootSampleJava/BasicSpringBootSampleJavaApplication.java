package com.adswizz.basicSpringBootSampleJava;

import com.ad.core.adData.AdResponseItem;
import com.ad.core.adFetcher.AdRequestConnection;
import com.ad.core.macro.MacroContext;
import com.adswizz.common.SDKError;
import com.adswizz.common.analytics.AnalyticsCollector;
import com.adswizz.common.analytics.AnalyticsConnector;
import com.adswizz.common.analytics.AnalyticsEvent;
import com.adswizz.common.macro.PlayerState;
import com.adswizz.common.macro.VASTErrorCode;
import com.adswizz.core.adFetcher.AdswizzAdRequest;
import com.adswizz.core.adFetcher.AdswizzAdZone;
import com.adswizz.core.privacy.CCPAConfig;
import com.adswizz.core.privacy.CCPAConsent;
import com.adswizz.core.privacy.GDPRConsent;
import com.adswizz.sdk.AdEventType;
import com.adswizz.sdk.AdReasonType;
import com.adswizz.sdk.AdswizzPrivacyConsent;
import com.adswizz.sdk.AdswizzSDK;
import com.adswizz.trackingengine.AdswizzTrackingEngine;
import com.adswizz.trackingengine.AdswizzTrackingException;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@RestController
public class BasicSpringBootSampleJavaApplication {

    private final AdswizzTrackingEngine trackingEngine = AdswizzTrackingEngine.getDefault();
    private final HashMap<String, byte[]> adPayloadStorage = new HashMap<>();

	public static void main(String[] args) {

        SpringApplication.run(BasicSpringBootSampleJavaApplication.class, args);

        AnalyticsConnector connector = new AnalyticsConnector() {
            @Override
            public @NotNull String getId() {
                return "your-connector-id";
            }

            @Override
            public void onLog(@NotNull AnalyticsEvent analyticsEvent) {
                System.out.println("log analytics event: " + analyticsEvent);
            }
        };

        AdswizzSDK.initialize("your-installation-id-here", () -> {
            AnalyticsCollector analyticsCollector = AdswizzSDK.getAnalytics();
            if (analyticsCollector != null) {
                analyticsCollector.add(connector);
            }
            System.out.println("SDK init completed");
        });

        AdswizzSDK.setLogger((logType, tag, message) -> System.out.println(logType + ":" + tag + ": " + message));


	}

    @GetMapping("/adRequest")
    public String callAdRequest() {
        long milis = System.currentTimeMillis();

        ArrayList<AdswizzAdZone> zones = new ArrayList<>();
        zones.add(new AdswizzAdZone.Builder()
                .withZoneId("18366")
                .withMaxAds(3)
                .withMaxDuration(Duration.ofMillis(40000))
                .build());

        // setting the initial macro context;
        MacroContext.Builder macroBuilder = new MacroContext.Builder()
                .withTransactionId(UUID.randomUUID().toString())
                .withPlayerState(List.of(PlayerState.FOREGROUND))
                .withIfa("adversier-id-random");

        // setting the consent
        AdswizzPrivacyConsent consent = new AdswizzPrivacyConsent.Builder()
                .withCCPA(new CCPAConfig(CCPAConsent.YES, CCPAConsent.NO, CCPAConsent.NOT_APPLICABLE))
                .withGDPR(GDPRConsent.GRANTED)
                .withGPC(true) // setting the Global Privacy Control Header
                .withTCFV2("user-consent-tcfv2")
                .build();

        AdswizzAdRequest adRequest;
        try {
            adRequest = new AdswizzAdRequest.Builder()
                    .withServer("demo.deliveryengine.adswizz.com")
                    .withHttpProtocol(AdswizzAdRequest.HttpProtocol.HTTPS)
                    .withZones(zones)
                    .withMacroContext(macroBuilder.build())
                    .withConsent(consent)
                    .build();

        } catch (SDKError e) {
            throw new RuntimeException(e);
        }
        AdRequestConnection requestConnection = new AdRequestConnection(adRequest);
        List<AdResponseItem> adResponseItems;
        try {
            adResponseItems = requestConnection.requestAds();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage());
        }
        StringBuilder returnStr = new StringBuilder();

        returnStr.append("{ \"ads\":[");
        milis = System.currentTimeMillis() - milis;

        for (int i = 0; i < adResponseItems.size(); i++) {
            String key = UUID.randomUUID().toString();
            returnStr.append("{ \"key\":\"").append(key).
                    append("\", \"payload_size\":\"").append(adResponseItems.get(i).getPayload().length).
                    append("\", \"adid\":\"").append(adResponseItems.get(i).getAdData().getId()).
                    append("\", \"duration\":\"").append(milis).
                    append("\"}");
            if (i < adResponseItems.size() - 1) {
                returnStr.append(",");
            }
            adPayloadStorage.put(key, adResponseItems.get(i).getPayload());
        }
        return returnStr.append("]}").toString();
    }


    @GetMapping("/started")
    public String started(@RequestParam(value = "unique_id", defaultValue = "1") String unique_id) {
        byte[] item = adPayloadStorage.get(unique_id);
        int initialSize = item.length;
        int newSize;
        long milis = System.currentTimeMillis();

        try {
            byte[] newPayload = trackingEngine.started(item, Duration.ofMillis(0), null);
            milis = System.currentTimeMillis() - milis;
            newSize = newPayload.length;
            adPayloadStorage.put(unique_id, newPayload);

            return "{ \"initialByteArraySize\":\"" + initialSize + ",\"newByteArraySize\":" + newSize +
                    ",\"durationMillis\":" + milis + "}";
        } catch (AdswizzTrackingException.InvalidPayload |
                 AdswizzTrackingException.InvalidArgument |
                 AdswizzTrackingException.InvalidElapsedTime |
                 AdswizzTrackingException.InternalError adswizzTrackingException) {
            return "{ \"exception\":\"" + adswizzTrackingException.getMessage() + ",\"durationMillis\":" + milis + "}";
        }
    }


    @GetMapping("/progress")
    public String progress(@RequestParam(value = "unique_id", defaultValue = "1") String unique_id, @RequestParam(value = "playhead", defaultValue = "0.0") Double playhead) {
        byte[] item = adPayloadStorage.get(unique_id);
        int initialSize = item.length;
        long milis = System.currentTimeMillis();
        var newSize = 0;
        System.out.println("*** Playhead: " + playhead);

        // example of how to:
        // override the current macro context if needed
        MacroContext newMacro = new MacroContext.Builder()
                .withAdPlayHead(playhead)
                .build();


        try {
            byte[] newPayload = trackingEngine.progress(item, Duration.ofMillis((int)(playhead*1000)), newMacro);
            milis = System.currentTimeMillis() - milis;
            adPayloadStorage.put(unique_id, newPayload);
            newSize = newPayload.length;

            return "{ \"initialByteArraySize\":\"" + initialSize + ",\"newByteArraySize\":" + newSize +
                    ",\"durationMillis\":" + milis + "}";
        } catch (Exception e) {
            return "{ \"exception\":\"" + e.getMessage() + ",\"durationMillis\":" + milis + "}";
        }
    }

    @GetMapping("/event")
    public String event(@RequestParam(value = "unique_id", defaultValue = "1") String unique_id, @RequestParam(value = "event", defaultValue = "unknown") String event) {
        byte[] payload = adPayloadStorage.get(unique_id);
        int initialSize = payload.length;
        long milis = System.currentTimeMillis();
        AdEventType eventType = AdEventType.OTHER_AD_INTERACTION;
        eventType = switch (event) {
            case "mute" -> AdEventType.MUTE;
            case "unmute" -> AdEventType.UNMUTE;
            case "pause" -> AdEventType.PAUSE;
            case "resume" -> AdEventType.RESUME;
            default -> eventType;
        };
        // example of how to:
        // override the current macro context if needed
        MacroContext newMacro = new MacroContext.Builder()
                .withPlayerState(List.of(PlayerState.BACKGROUND))
                .build();

        try {
            byte[] newPayload = trackingEngine.onEvent(payload, null, eventType, newMacro);
            adPayloadStorage.put(unique_id, newPayload);
            milis = System.currentTimeMillis() - milis;

            return "{ \"initialByteArraySize\":\"" + initialSize + ",\"durationMillis\":" + milis + "}";
        } catch (Exception e) {
            return "{ \"exception\":\"" + e.getMessage() + ",\"durationMillis\":" + milis + "}";
        }
    }

    @GetMapping("/ended")
    public String ended(@RequestParam(value = "unique_id", defaultValue = "1") String unique_id) {
        byte[] item = adPayloadStorage.get(unique_id);
        int initialSize = item.length;
        long milis = System.currentTimeMillis();

        try {
            byte[] newPayload = trackingEngine.ended(item, null, AdReasonType.AD_COMPLETED, null);
            milis = System.currentTimeMillis() - milis;
            adPayloadStorage.put(unique_id, newPayload);
            return "{ \"initialByteArraySize\":\"" + initialSize + ",\"newByteArraySize\":" + newPayload.length + ",\"durationMillis\":" + milis + "}";
        } catch (AdswizzTrackingException.InvalidPayload e) {
            return "{ \"exception\":\"Invalid payload,\"durationMillis\":" + milis + "}";
        } catch (AdswizzTrackingException e) {
            return "{ \"exception\":\"" + e.getMessage() + ",\"durationMillis\":" + milis + "}";
        }
    }

    @GetMapping("/endedWithError")
    public String endedWithError(@RequestParam(value = "unique_id", defaultValue = "1") String unique_id) {
        byte[] item = adPayloadStorage.get(unique_id);
        int initialSize = item.length;
        long milis = System.currentTimeMillis();

        // example of how to:
        // override the current macro context if needed
        MacroContext newMacro = new MacroContext.Builder()
                .withErrorCode(VASTErrorCode.AD_BREAK_SHORTENED)
                .build();

        try {
            byte[] newPayload = trackingEngine.ended(item, null, AdReasonType.AD_SKIPPED_WITH_ERROR, newMacro);
            adPayloadStorage.put(unique_id, newPayload);
            milis = System.currentTimeMillis() - milis;

            return "{ \"initialByteArraySize\":\"" + initialSize + ",\"durationMillis\":" + milis + "}";
        } catch (AdswizzTrackingException e) {
            return "{ \"exception\":\"" + e.getMessage() + ",\"durationMillis\":" + milis + "}";
        }

    }

    @GetMapping("/errorTracking")
    public String errorTrackingEngine(@RequestParam(value = "unique_id", defaultValue = "1") String unique_id) {
        byte[] item = adPayloadStorage.get(unique_id);
        int initialSize = item.length;
        long milis = System.currentTimeMillis();

        // example of how to:
        // override the current macro context if needed
        MacroContext newMacro = new MacroContext.Builder()
                .withErrorCode(VASTErrorCode.RESPONSE_NOT_SUPPORTED) // or your custom error code
                .build();

        try {
            byte[] newPayload = trackingEngine.error(item, null, newMacro);
            adPayloadStorage.put(unique_id, newPayload);
            milis = System.currentTimeMillis() - milis;

            return "{ \"initialByteArraySize\":\"" + initialSize + ",\"durationMillis\":" + milis + "}";
        } catch (AdswizzTrackingException e) {
            return "{ \"exception\":\"" + e.getMessage() + ",\"durationMillis\":" + milis + "}";
        }

    }
}
