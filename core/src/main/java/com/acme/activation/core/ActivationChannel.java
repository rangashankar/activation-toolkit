package com.acme.activation.core;

public interface ActivationChannel {
    String id();

    boolean isAvailable();

    void startDiscovery(DiscoveryListener listener);

    void stopDiscovery();

    ActivationResult activate(ActivationRequest request);
}
