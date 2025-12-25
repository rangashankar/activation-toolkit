package com.acme.activation.qr;

import com.acme.activation.core.DeviceChallenge;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public final class DeviceChallengeQrCodec implements QrCodec<DeviceChallenge> {
    public static final String TYPE = "dev_chal_v1";

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public String encode(DeviceChallenge payload) {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("t", TYPE);
        values.put("deviceId", payload.getDeviceId());
        values.put("deviceNonce", Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getDeviceNonce()));
        values.put("deviceCounter", payload.getDeviceCounter());
        values.put("devicePubKey", Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getDevicePublicKey()));
        return SimpleJson.stringify(values);
    }

    @Override
    public DeviceChallenge decode(String data) {
        Map<String, String> values = SimpleJson.parseFlatObject(data);
        String type = values.get("t");
        if (!TYPE.equals(type)) {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        String deviceId = values.get("deviceId");
        byte[] deviceNonce = Base64.getUrlDecoder().decode(values.get("deviceNonce"));
        long deviceCounter = Long.parseLong(values.get("deviceCounter"));
        byte[] devicePubKey = Base64.getUrlDecoder().decode(values.get("devicePubKey"));
        return new DeviceChallenge(deviceId, deviceNonce, deviceCounter, devicePubKey);
    }
}
