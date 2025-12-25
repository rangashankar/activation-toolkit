package com.acme.activation.qr;

import com.acme.activation.core.ActivationRequest;
import com.acme.activation.core.DeviceChallenge;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QrCodecTest {
    private final SecureRandom random = new SecureRandom();

    @Test
    void roundTripDeviceChallenge() {
        byte[] nonce = randomBytes(16);
        byte[] pubKey = randomBytes(64);
        DeviceChallenge challenge = new DeviceChallenge("device-123", nonce, 7L, pubKey);

        DeviceChallengeQrCodec codec = new DeviceChallengeQrCodec();
        String encoded = codec.encode(challenge);
        DeviceChallenge decoded = codec.decode(encoded);

        assertEquals(challenge.getDeviceId(), decoded.getDeviceId());
        assertEquals(challenge.getDeviceCounter(), decoded.getDeviceCounter());
        assertTrue(Arrays.equals(challenge.getDeviceNonce(), decoded.getDeviceNonce()));
        assertTrue(Arrays.equals(challenge.getDevicePublicKey(), decoded.getDevicePublicKey()));
    }

    @Test
    void roundTripActivationRequest() {
        ActivationRequest request = new ActivationRequest(
                "device-abc",
                9L,
                randomBytes(12),
                randomBytes(12),
                randomBytes(32),
                randomBytes(64)
        );

        ActivationRequestQrCodec codec = new ActivationRequestQrCodec();
        String encoded = codec.encode(request);
        ActivationRequest decoded = codec.decode(encoded);

        assertEquals(request.getDeviceId(), decoded.getDeviceId());
        assertEquals(request.getDeviceCounter(), decoded.getDeviceCounter());
        assertTrue(Arrays.equals(request.getDeviceNonce(), decoded.getDeviceNonce()));
        assertTrue(Arrays.equals(request.getActivationNonce(), decoded.getActivationNonce()));
        assertTrue(Arrays.equals(request.getSessionKey(), decoded.getSessionKey()));
        assertTrue(Arrays.equals(request.getSignature(), decoded.getSignature()));
    }

    private byte[] randomBytes(int length) {
        byte[] data = new byte[length];
        random.nextBytes(data);
        return data;
    }
}
