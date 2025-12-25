package com.acme.activation.core;

import java.util.List;

public interface OfflineQueue {
    void enqueue(ActivationRequest request);

    List<ActivationRequest> drain();
}
