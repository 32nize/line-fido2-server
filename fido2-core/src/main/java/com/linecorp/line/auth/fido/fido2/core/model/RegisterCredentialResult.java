package com.linecorp.line.auth.fido.fido2.core.model;

import lombok.Data;

@Data
public class RegisterCredentialResult {
    private String credentialId;
    private String publicKeyPem;
    private long signCount;
}
