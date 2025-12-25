package com.example.activation.crypto;

import com.example.activation.core.ActivationRequest;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public final class Crypto {
    private Crypto() {
    }

    public static byte[] buildActivationSignatureInput(ActivationRequest request) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeBytes(out, request.getDeviceId().getBytes(StandardCharsets.UTF_8));
        writeLong(out, request.getDeviceCounter());
        writeBytes(out, request.getDeviceNonce());
        writeBytes(out, request.getActivationNonce());
        writeBytes(out, request.getSessionKey());
        return out.toByteArray();
    }

    public static boolean verifySignature(byte[] publicKeyBytes, String algorithm, byte[] message, byte[] signatureBytes)
            throws Exception {
        PublicKey publicKey = decodePublicKey(publicKeyBytes, algorithm);
        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(message);
        return signature.verify(signatureBytes);
    }

    public static byte[] sign(byte[] privateKeyBytes, String algorithm, byte[] message) throws Exception {
        PrivateKey privateKey = decodePrivateKey(privateKeyBytes, algorithm);
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(message);
        return signature.sign();
    }

    private static PublicKey decodePublicKey(byte[] publicKeyBytes, String algorithm) throws Exception {
        String keyAlgorithm = algorithm.contains("ECDSA") ? "EC" : "RSA";
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return KeyFactory.getInstance(keyAlgorithm).generatePublic(keySpec);
    }

    private static PrivateKey decodePrivateKey(byte[] privateKeyBytes, String algorithm) throws Exception {
        String keyAlgorithm = algorithm.contains("ECDSA") ? "EC" : "RSA";
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return KeyFactory.getInstance(keyAlgorithm).generatePrivate(keySpec);
    }

    private static void writeBytes(ByteArrayOutputStream out, byte[] data) {
        if (data == null) {
            writeInt(out, 0);
            return;
        }
        writeInt(out, data.length);
        out.writeBytes(data);
    }

    private static void writeLong(ByteArrayOutputStream out, long value) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(value);
        out.writeBytes(buffer.array());
    }

    private static void writeInt(ByteArrayOutputStream out, int value) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(value);
        out.writeBytes(buffer.array());
    }
}
