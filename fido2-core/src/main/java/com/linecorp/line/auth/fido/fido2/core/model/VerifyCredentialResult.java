package com.linecorp.line.auth.fido.fido2.core.model;

import lombok.Data;

@Data
public class VerifyCredentialResult {
    private boolean success;
    private String credentialId;
    private long signCount;
}
