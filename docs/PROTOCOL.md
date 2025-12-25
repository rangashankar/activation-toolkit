QR Activation Protocol (v1, clock-free)

Goals
- Offline activation without trusted device clocks.
- Strong replay protection using monotonic counters and nonce reuse detection.
- Minimal payloads for QR scanning.

Entities
- Device: IoT device that displays a QR challenge and validates activation.
- App: Mobile app that reads the challenge and returns activation.
- Registry: Store of device public keys or manufacturer certificates.

Payloads

DeviceChallenge (QR from device to app)
```json
{
  "t": "dev_chal_v1",
  "deviceId": "D123",
  "deviceNonce": "base64url(16)",
  "deviceCounter": 42,
  "devicePubKey": "base64url(X509 public key)"
}
```

ActivationRequest (QR from app to device)
```json
{
  "t": "app_act_v1",
  "deviceId": "D123",
  "deviceCounter": 42,
  "deviceNonce": "base64url(16)",
  "activationNonce": "base64url(16)",
  "sessionKey": "base64url(32)",
  "sig": "base64url(signature)"
}
```

Signature input
- Concatenate fields in this order with length-prefixing for byte arrays:
  - deviceId (UTF-8 bytes)
  - deviceCounter (8 bytes, big-endian)
  - deviceNonce
  - activationNonce
  - sessionKey
- See `Crypto.buildActivationSignatureInput` for the canonical serialization.

Signature algorithms
- Recommended: SHA256withECDSA (P-256), or SHA256withRSA.
- Public keys are X.509 encoded.

Replay protection (no time)
- Device maintains a monotonic `deviceCounter` and increments after successful activation.
- Device stores recent `activationNonce` values (bounded cache).
- Device rejects requests with stale counters or repeated nonces.

Failure modes and guidance
- If counter is stale or nonce already used, return a hard failure.
- If signature verification fails, return a hard failure.
- If QR payload parsing fails, return an invalid payload error.

Extensibility
- New versions add new `t` values (e.g., dev_chal_v2).
- The `t` field routes to the matching codec.
