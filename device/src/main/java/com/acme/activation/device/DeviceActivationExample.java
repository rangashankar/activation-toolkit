package com.acme.activation.device;

import com.acme.activation.core.ActivationRequest;
import com.acme.activation.core.ActivationResult;
import com.acme.activation.core.DeviceChallenge;
import com.acme.activation.crypto.Crypto;
import com.acme.activation.qr.ActivationRequestQrCodec;
import com.acme.activation.qr.DeviceChallengeQrCodec;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

public final class DeviceActivationExample {
    private static final String SIGNATURE_ALGORITHM = "SHA256withECDSA";

    public static void main(String[] args) throws Exception {
        KeyPair keyPair = KeyPairGenerator.getInstance("EC").generateKeyPair();
        byte[] devicePublicKey = keyPair.getPublic().getEncoded();
        byte[] devicePrivateKey = keyPair.getPrivate().getEncoded();

        SecureRandom random = new SecureRandom();
        byte[] deviceNonce = new byte[16];
        random.nextBytes(deviceNonce);

        DeviceChallenge challenge = new DeviceChallenge("device-001", deviceNonce, 1L, devicePublicKey);
        DeviceChallengeQrCodec challengeCodec = new DeviceChallengeQrCodec();
        String challengeQr = challengeCodec.encode(challenge);

        DeviceChallenge decodedChallenge = challengeCodec.decode(challengeQr);

        byte[] activationNonce = new byte[16];
        random.nextBytes(activationNonce);
        byte[] sessionKey = new byte[32];
        random.nextBytes(sessionKey);

        ActivationRequest unsignedRequest = new ActivationRequest(
                decodedChallenge.getDeviceId(),
                decodedChallenge.getDeviceCounter(),
                decodedChallenge.getDeviceNonce(),
                activationNonce,
                sessionKey,
                new byte[0]
        );

        byte[] message = Crypto.buildActivationSignatureInput(unsignedRequest);
        byte[] signature = Crypto.sign(devicePrivateKey, SIGNATURE_ALGORITHM, message);

        ActivationRequest signedRequest = new ActivationRequest(
                unsignedRequest.getDeviceId(),
                unsignedRequest.getDeviceCounter(),
                unsignedRequest.getDeviceNonce(),
                unsignedRequest.getActivationNonce(),
                unsignedRequest.getSessionKey(),
                signature
        );

        ActivationRequestQrCodec requestCodec = new ActivationRequestQrCodec();
        String requestQr = requestCodec.encode(signedRequest);
        ActivationRequest decodedRequest = requestCodec.decode(requestQr);

        ReplayCache replayCache = new InMemoryReplayCache(128);
        DeviceActivationValidator validator = new DeviceActivationValidator(replayCache, SIGNATURE_ALGORITHM);
        ActivationResult result = validator.validate(decodedChallenge, decodedRequest);
        System.out.println(result.getStatus() + ": " + result.getMessage());
    }
}
