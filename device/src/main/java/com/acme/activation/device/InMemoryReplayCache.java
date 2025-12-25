package com.acme.activation.device;

import java.util.ArrayDeque;
import java.util.Base64;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public final class InMemoryReplayCache implements ReplayCache {
    private final AtomicLong lastCounter = new AtomicLong(-1);
    private final Set<String> nonces = new HashSet<>();
    private final Deque<String> nonceOrder = new ArrayDeque<>();
    private final int maxNonces;

    public InMemoryReplayCache(int maxNonces) {
        this.maxNonces = maxNonces;
    }

    @Override
    public synchronized boolean isCounterFresh(long counter) {
        return counter > lastCounter.get();
    }

    @Override
    public synchronized boolean isNonceUsed(byte[] nonce) {
        return nonces.contains(encodeNonce(nonce));
    }

    @Override
    public synchronized void markUsed(long counter, byte[] nonce) {
        lastCounter.set(counter);
        String encoded = encodeNonce(nonce);
        if (nonces.add(encoded)) {
            nonceOrder.addLast(encoded);
            trimIfNeeded();
        }
    }

    private void trimIfNeeded() {
        while (nonceOrder.size() > maxNonces) {
            String oldest = nonceOrder.removeFirst();
            nonces.remove(oldest);
        }
    }

    private String encodeNonce(byte[] nonce) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(nonce);
    }
}
