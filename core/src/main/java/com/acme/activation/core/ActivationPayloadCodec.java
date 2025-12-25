package com.acme.activation.core;

public interface ActivationPayloadCodec<T> {
    String type();

    String encode(T payload);

    T decode(String data);
}
