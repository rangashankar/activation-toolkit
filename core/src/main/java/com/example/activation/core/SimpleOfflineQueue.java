package com.example.activation.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SimpleOfflineQueue implements OfflineQueue {
    private final List<ActivationRequest> buffer = new ArrayList<>();

    @Override
    public synchronized void enqueue(ActivationRequest request) {
        buffer.add(request);
    }

    @Override
    public synchronized List<ActivationRequest> drain() {
        if (buffer.isEmpty()) {
            return Collections.emptyList();
        }
        List<ActivationRequest> snapshot = new ArrayList<>(buffer);
        buffer.clear();
        return snapshot;
    }
}
