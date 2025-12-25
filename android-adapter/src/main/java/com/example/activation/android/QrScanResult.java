package com.example.activation.android;

import com.example.activation.core.ActivationRequest;
import com.example.activation.core.DeviceChallenge;

public final class QrScanResult {
    public enum Type {
        DEVICE_CHALLENGE,
        ACTIVATION_REQUEST
    }

    private final Type type;
    private final DeviceChallenge deviceChallenge;
    private final ActivationRequest activationRequest;

    private QrScanResult(Type type, DeviceChallenge deviceChallenge, ActivationRequest activationRequest) {
        this.type = type;
        this.deviceChallenge = deviceChallenge;
        this.activationRequest = activationRequest;
    }

    public static QrScanResult deviceChallenge(DeviceChallenge challenge) {
        return new QrScanResult(Type.DEVICE_CHALLENGE, challenge, null);
    }

    public static QrScanResult activationRequest(ActivationRequest request) {
        return new QrScanResult(Type.ACTIVATION_REQUEST, null, request);
    }

    public Type getType() {
        return type;
    }

    public DeviceChallenge getDeviceChallenge() {
        return deviceChallenge;
    }

    public ActivationRequest getActivationRequest() {
        return activationRequest;
    }
}
