package com.example.activation.core;

import java.util.Optional;

public interface CredentialStore {
    Optional<DeviceCredentials> get(String deviceId);

    void put(DeviceCredentials credentials);
}
