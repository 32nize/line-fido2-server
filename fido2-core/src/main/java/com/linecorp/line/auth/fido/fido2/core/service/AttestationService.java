package com.linecorp.line.auth.fido.fido2.core.service;

import com.linecorp.line.auth.fido.fido2.common.AuthenticatorSelectionCriteria;
import com.linecorp.line.auth.fido.fido2.common.server.ServerAuthenticatorAttestationResponse;
import com.linecorp.line.auth.fido.fido2.server.attestation.AttestationVerificationResult;
import com.linecorp.line.auth.fido.fido2.server.model.AttestationObject;

/**
 * Core-facing AttestationService API. Implementation is delegated to the existing server-side service via adapter.
 */
public interface AttestationService {
    AttestationVerificationResult verifyAttestation(byte[] clientDataHsh, AttestationObject attestationObject);
    AttestationObject getAttestationObject(ServerAuthenticatorAttestationResponse attestationResponse);
    void attestationObjectValidationCheck(String rpId, AuthenticatorSelectionCriteria authenticatorSelection, AttestationObject attestationObject);
    void verifyAttestationCertificate(AttestationObject attestationObject, AttestationVerificationResult attestationVerificationResult);
}
