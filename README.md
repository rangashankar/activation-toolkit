Activation Toolkit (QR-first, offline-safe)

Overview
- Cross-platform activation protocol with a Java reference implementation.
- Offline-safe QR activation (no device clock required) using counters + nonces.
- Modular design for new channels (BLE/NFC) and payload versions.

Modules
- core: activation SPI, models, offline queue, engine
- crypto: signature input builder + sign/verify helpers
- qr: QR payload codecs for device challenge and activation request
- device: device-side validator + replay cache + example
- android-adapter: Android-friendly QR scan adapter (no UI dependencies)

Offline QR flow (clock-free)
1) Device shows DeviceChallenge QR with deviceId, deviceNonce, deviceCounter, devicePubKey.
2) App verifies device public key (or certificate chain in future).
3) App builds ActivationRequest using deviceNonce + counter, signs it, shows QR back to device.
4) Device verifies signature, checks counter monotonicity and nonce reuse, then activates.

Key files
- docs/PROTOCOL.md: payload schema + signature input rules
- docs/ANDROID_INTEGRATION.md: Android wiring guide

Build and test
- gradle test
- gradle :qr:test :device:test

Quick usage (device-side validation)
```java
ReplayCache replayCache = new InMemoryReplayCache(128);
DeviceActivationValidator validator = new DeviceActivationValidator(replayCache, "SHA256withECDSA");
ActivationResult result = validator.validate(challenge, request);
```

Notes
- For iOS, mirror the protocol and payload codecs in Swift to keep interoperability.
- Replace InMemoryReplayCache with persistent storage on real devices.
