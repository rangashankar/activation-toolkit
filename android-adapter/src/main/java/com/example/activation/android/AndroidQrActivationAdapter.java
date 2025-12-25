package com.example.activation.android;

import com.example.activation.core.ActivationRequest;
import com.example.activation.core.DeviceChallenge;
import com.example.activation.qr.ActivationRequestQrCodec;
import com.example.activation.qr.DeviceChallengeQrCodec;
import com.example.activation.qr.SimpleJson;

import java.util.Map;

public final class AndroidQrActivationAdapter {
    private final DeviceChallengeQrCodec deviceCodec = new DeviceChallengeQrCodec();
    private final ActivationRequestQrCodec activationCodec = new ActivationRequestQrCodec();

    public QrScanResult handleScan(String qrText) {
        String type = extractType(qrText);
        if (DeviceChallengeQrCodec.TYPE.equals(type)) {
            DeviceChallenge challenge = deviceCodec.decode(qrText);
            return QrScanResult.deviceChallenge(challenge);
        }
        if (ActivationRequestQrCodec.TYPE.equals(type)) {
            ActivationRequest request = activationCodec.decode(qrText);
            return QrScanResult.activationRequest(request);
        }
        throw new IllegalArgumentException("Unsupported QR type: " + type);
    }

    private String extractType(String qrText) {
        Map<String, String> values = SimpleJson.parseFlatObject(qrText);
        String type = values.get("t");
        if (type == null) {
            throw new IllegalArgumentException("Missing QR type field 't'");
        }
        return type;
    }
}
