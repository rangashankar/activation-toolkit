package com.example.activation.core;

import java.util.Arrays;

public final class DeviceChallenge {
    private final String deviceId;
    private final byte[] deviceNonce;
    private final long deviceCounter;
    private final byte[] devicePublicKey;

    public DeviceChallenge(String deviceId, byte[] deviceNonce, long deviceCounter, byte[] devicePublicKey) {
        this.deviceId = deviceId;
        this.deviceNonce = deviceNonce;
        this.deviceCounter = deviceCounter;
        this.devicePublicKey = devicePublicKey;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public byte[] getDeviceNonce() {
        return deviceNonce;
    }

    public long getDeviceCounter() {
        return deviceCounter;
    }

    public byte[] getDevicePublicKey() {
        return devicePublicKey;
    }

    @Override
    public String toString() {
        return "DeviceChallenge{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceNonce=" + Arrays.toString(deviceNonce) +
                ", deviceCounter=" + deviceCounter +
                ", devicePublicKey=" + Arrays.toString(devicePublicKey) +
                '}';
    }
}
