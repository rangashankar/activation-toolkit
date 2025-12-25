package com.acme.activation.qr;

import com.acme.activation.core.ActivationRequest;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ActivationRequestQrCodec implements QrCodec<ActivationRequest> {
    public static final String TYPE = "app_act_v1";

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public String encode(ActivationRequest payload) {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("t", TYPE);
        values.put("deviceId", payload.getDeviceId());
        values.put("deviceCounter", payload.getDeviceCounter());
        values.put("deviceNonce", Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getDeviceNonce()));
        values.put("activationNonce", Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getActivationNonce()));
        values.put("sessionKey", Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getSessionKey()));
        values.put("sig", Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getSignature()));
        return SimpleJson.stringify(values);
    }

    @Override
    public ActivationRequest decode(String data) {
        Map<String, String> values = SimpleJson.parseFlatObject(data);
        String type = values.get("t");
        if (!TYPE.equals(type)) {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        String deviceId = values.get("deviceId");
        long deviceCounter = Long.parseLong(values.get("deviceCounter"));
        byte[] deviceNonce = Base64.getUrlDecoder().decode(values.get("deviceNonce"));
        byte[] activationNonce = Base64.getUrlDecoder().decode(values.get("activationNonce"));
        byte[] sessionKey = Base64.getUrlDecoder().decode(values.get("sessionKey"));
        byte[] signature = Base64.getUrlDecoder().decode(values.get("sig"));
        return new ActivationRequest(deviceId, deviceCounter, deviceNonce, activationNonce, sessionKey, signature);
    }
}
