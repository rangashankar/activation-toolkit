package com.acme.activation.core;

public final class DeviceCredentials {
    private final String deviceId;
    private final byte[] devicePublicKey;
    private final byte[] sharedSecret;

    public DeviceCredentials(String deviceId, byte[] devicePublicKey, byte[] sharedSecret) {
        this.deviceId = deviceId;
        this.devicePublicKey = devicePublicKey;
        this.sharedSecret = sharedSecret;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public byte[] getDevicePublicKey() {
        return devicePublicKey;
    }

    public byte[] getSharedSecret() {
        return sharedSecret;
    }
}
