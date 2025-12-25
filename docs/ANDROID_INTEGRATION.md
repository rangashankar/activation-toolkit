Android Integration Guide

Goal
- Connect a QR scanner to the activation toolkit without Android SDK dependencies inside the toolkit.
- Keep parsing/validation in shared Java modules while UI stays in your app.

Recommended wiring
1) Use a QR scanner of your choice (ML Kit, ZXing, or CameraX pipeline).
2) When the scanner outputs a QR string, pass it to AndroidQrActivationAdapter.
3) Route the result based on type.

Example usage (app layer)
```java
AndroidQrActivationAdapter adapter = new AndroidQrActivationAdapter();

String qrText = /* QR scanner output */;
QrScanResult result = adapter.handleScan(qrText);

switch (result.getType()) {
    case DEVICE_CHALLENGE:
        DeviceChallenge challenge = result.getDeviceChallenge();
        // Build ActivationRequest, sign, then display QR
        break;
    case ACTIVATION_REQUEST:
        ActivationRequest request = result.getActivationRequest();
        // Optional: validate on-device or send to backend
        break;
}
```

Scanner integration notes
- ML Kit: use BarcodeScanning API and pass the raw value as qrText.
- ZXing: use IntentIntegrator or embedded scanner and pass the contents.
- Ensure raw QR text is preserved; avoid trimming or line-ending changes.

App signing responsibilities
- The app needs a signing key (private) or shared secret to produce ActivationRequest signatures.
- Store private keys in Android Keystore; rotate keys via your provisioning process.

Device registry
- The app must know device public keys or a manufacturer root certificate for verification.
- Implement DeviceRegistry/CredentialStore in your app module.

Error handling
- handleScan throws IllegalArgumentException for unsupported/invalid QR payloads.
- Treat failures as non-retryable unless the scanner returns a partial payload.
