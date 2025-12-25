package com.acme.activation.core;

import java.util.Optional;

public interface DeviceRegistry {
    Optional<DeviceInfo> lookup(String deviceId);

    void register(DeviceInfo info);
}
