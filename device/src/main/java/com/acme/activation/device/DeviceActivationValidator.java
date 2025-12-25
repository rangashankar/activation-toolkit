package com.acme.activation.device;

import com.acme.activation.core.ActivationRequest;
import com.acme.activation.core.ActivationResult;
import com.acme.activation.core.DeviceChallenge;
import com.acme.activation.crypto.Crypto;

import java.util.Arrays;

public final class DeviceActivationValidator {
    private final ReplayCache replayCache;
    private final String signatureAlgorithm;

    public DeviceActivationValidator(ReplayCache replayCache, String signatureAlgorithm) {
        this.replayCache = replayCache;
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public ActivationResult validate(DeviceChallenge challenge, ActivationRequest request) {
        if (!challenge.getDeviceId().equals(request.getDeviceId())) {
            return ActivationResult.failed("Device ID mismatch");
        }
        if (challenge.getDeviceCounter() != request.getDeviceCounter()) {
            return ActivationResult.failed("Device counter mismatch");
        }
        if (!Arrays.equals(challenge.getDeviceNonce(), request.getDeviceNonce())) {
            return ActivationResult.failed("Device nonce mismatch");
        }
        if (!replayCache.isCounterFresh(request.getDeviceCounter())) {
            return ActivationResult.failed("Replay detected: counter");
        }
        if (replayCache.isNonceUsed(request.getActivationNonce())) {
            return ActivationResult.failed("Replay detected: nonce");
        }
        try {
            byte[] message = Crypto.buildActivationSignatureInput(request);
            boolean ok = Crypto.verifySignature(
                    challenge.getDevicePublicKey(),
                    signatureAlgorithm,
                    message,
                    request.getSignature()
            );
            if (!ok) {
                return ActivationResult.failed("Invalid signature");
            }
        } catch (Exception e) {
            return ActivationResult.failed("Signature check failed: " + e.getMessage());
        }
        replayCache.markUsed(request.getDeviceCounter(), request.getActivationNonce());
        return ActivationResult.success("Activation accepted");
    }
}
