package com.acme.activation.core;

import java.util.Map;
import java.util.Objects;

public final class ActivationEngine {
    private final Map<String, ActivationChannel> channels;
    private final OfflineQueue offlineQueue;

    public ActivationEngine(Map<String, ActivationChannel> channels, OfflineQueue offlineQueue) {
        this.channels = Objects.requireNonNull(channels, "channels");
        this.offlineQueue = Objects.requireNonNull(offlineQueue, "offlineQueue");
    }

    public ActivationResult activate(String channelId, ActivationRequest request) {
        ActivationChannel channel = channels.get(channelId);
        if (channel == null) {
            return ActivationResult.failed("Unknown channel: " + channelId);
        }
        if (!channel.isAvailable()) {
            offlineQueue.enqueue(request);
            return ActivationResult.retryLater("Channel unavailable; queued for later");
        }
        return channel.activate(request);
    }
}
