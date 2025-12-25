package com.acme.activation.core;

public interface DiscoveryListener {
    void onDeviceFound(DeviceInfo deviceInfo);

    void onError(String message);
}
