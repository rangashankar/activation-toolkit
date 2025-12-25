package com.example.activation.core;

public interface ActivationPolicy {
    boolean isReplay(long deviceCounter, byte[] activationNonce);

    void markUsed(long deviceCounter, byte[] activationNonce);
}
