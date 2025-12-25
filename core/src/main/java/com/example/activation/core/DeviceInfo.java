package com.example.activation.core;

public final class DeviceInfo {
    private final String deviceId;
    private final String model;
    private final byte[] devicePublicKey;

    public DeviceInfo(String deviceId, String model, byte[] devicePublicKey) {
        this.deviceId = deviceId;
        this.model = model;
        this.devicePublicKey = devicePublicKey;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getModel() {
        return model;
    }

    public byte[] getDevicePublicKey() {
        return devicePublicKey;
    }
}
