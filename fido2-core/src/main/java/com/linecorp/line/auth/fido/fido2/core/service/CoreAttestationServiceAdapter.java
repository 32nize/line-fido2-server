package com.linecorp.line.auth.fido.fido2.core.service;

import com.linecorp.line.auth.fido.fido2.common.AuthenticatorSelectionCriteria;
import com.linecorp.line.auth.fido.fido2.common.server.ServerAuthenticatorAttestationResponse;
import com.linecorp.line.auth.fido.fido2.server.attestation.AttestationVerificationResult;
import com.linecorp.line.auth.fido.fido2.server.model.AttestationObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Adapter exposing existing server AttestationService via the core API.
 */
@Component
public class CoreAttestationServiceAdapter implements AttestationService {

    private final com.linecorp.line.auth.fido.fido2.server.service.AttestationService delegate;

    @Autowired
    public CoreAttestationServiceAdapter(com.linecorp.line.auth.fido.fido2.server.service.AttestationService delegate) {
        this.delegate = delegate;
    }

    @Override
    public AttestationVerificationResult verifyAttestation(byte[] clientDataHsh, AttestationObject attestationObject) {
        return delegate.verifyAttestation(clientDataHsh, attestationObject);
    }

    @Override
    public AttestationObject getAttestationObject(ServerAuthenticatorAttestationResponse attestationResponse) {
        return delegate.getAttestationObject(attestationResponse);
    }

    @Override
    public void attestationObjectValidationCheck(String rpId, AuthenticatorSelectionCriteria authenticatorSelection, AttestationObject attestationObject) {
        delegate.attestationObjectValidationCheck(rpId, authenticatorSelection, attestationObject);
    }

    @Override
    public void verifyAttestationCertificate(AttestationObject attestationObject, AttestationVerificationResult attestationVerificationResult) {
        delegate.verifyAttestationCertificate(attestationObject, attestationVerificationResult);
    }
}
