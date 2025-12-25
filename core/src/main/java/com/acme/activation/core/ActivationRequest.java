package com.acme.activation.core;

import java.util.Arrays;

public final class ActivationRequest {
    private final String deviceId;
    private final long deviceCounter;
    private final byte[] deviceNonce;
    private final byte[] activationNonce;
    private final byte[] sessionKey;
    private final byte[] signature;

    public ActivationRequest(
            String deviceId,
            long deviceCounter,
            byte[] deviceNonce,
            byte[] activationNonce,
            byte[] sessionKey,
            byte[] signature
    ) {
        this.deviceId = deviceId;
        this.deviceCounter = deviceCounter;
        this.deviceNonce = deviceNonce;
        this.activationNonce = activationNonce;
        this.sessionKey = sessionKey;
        this.signature = signature;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public long getDeviceCounter() {
        return deviceCounter;
    }

    public byte[] getDeviceNonce() {
        return deviceNonce;
    }

    public byte[] getActivationNonce() {
        return activationNonce;
    }

    public byte[] getSessionKey() {
        return sessionKey;
    }

    public byte[] getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        return "ActivationRequest{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceCounter=" + deviceCounter +
                ", deviceNonce=" + Arrays.toString(deviceNonce) +
                ", activationNonce=" + Arrays.toString(activationNonce) +
                ", sessionKey=" + Arrays.toString(sessionKey) +
                '}';
    }
}
