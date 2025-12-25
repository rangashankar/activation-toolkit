package com.example.activation.device;

import com.example.activation.core.ActivationRequest;
import com.example.activation.core.ActivationResult;
import com.example.activation.core.DeviceChallenge;
import com.example.activation.crypto.Crypto;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeviceActivationValidatorTest {
    private static final String SIGNATURE_ALGORITHM = "SHA256withECDSA";

    @Test
    void validatesAndRejectsReplay() throws Exception {
        KeyPair keyPair = KeyPairGenerator.getInstance("EC").generateKeyPair();
        byte[] publicKey = keyPair.getPublic().getEncoded();
        byte[] privateKey = keyPair.getPrivate().getEncoded();

        SecureRandom random = new SecureRandom();
        byte[] deviceNonce = randomBytes(random, 16);

        DeviceChallenge challenge = new DeviceChallenge("device-001", deviceNonce, 1L, publicKey);

        byte[] activationNonce = randomBytes(random, 16);
        byte[] sessionKey = randomBytes(random, 32);

        ActivationRequest unsignedRequest = new ActivationRequest(
                challenge.getDeviceId(),
                challenge.getDeviceCounter(),
                challenge.getDeviceNonce(),
                activationNonce,
                sessionKey,
                new byte[0]
        );
        byte[] message = Crypto.buildActivationSignatureInput(unsignedRequest);
        byte[] signature = Crypto.sign(privateKey, SIGNATURE_ALGORITHM, message);

        ActivationRequest request = new ActivationRequest(
                unsignedRequest.getDeviceId(),
                unsignedRequest.getDeviceCounter(),
                unsignedRequest.getDeviceNonce(),
                unsignedRequest.getActivationNonce(),
                unsignedRequest.getSessionKey(),
                signature
        );

        DeviceActivationValidator validator = new DeviceActivationValidator(new InMemoryReplayCache(32), SIGNATURE_ALGORITHM);
        ActivationResult first = validator.validate(challenge, request);
        ActivationResult replay = validator.validate(challenge, request);

        assertEquals(ActivationResult.Status.SUCCESS, first.getStatus());
        assertEquals(ActivationResult.Status.FAILED, replay.getStatus());
    }

    private byte[] randomBytes(SecureRandom random, int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }
}
