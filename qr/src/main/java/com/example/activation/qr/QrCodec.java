package com.example.activation.qr;

public interface QrCodec<T> {
    String type();

    String encode(T payload);

    T decode(String data);
}
