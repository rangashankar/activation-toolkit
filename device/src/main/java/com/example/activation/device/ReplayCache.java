package com.example.activation.device;

public interface ReplayCache {
    boolean isCounterFresh(long counter);

    boolean isNonceUsed(byte[] nonce);

    void markUsed(long counter, byte[] nonce);
}
