package com.linecorp.line.auth.fido.fido2.core.service;

import com.linecorp.line.auth.fido.fido2.common.server.RegisterCredentialResult;
import com.linecorp.line.auth.fido.fido2.common.server.VerifyCredentialResult;
import com.linecorp.line.auth.fido.fido2.common.server.ServerRegPublicKeyCredential;
import com.linecorp.line.auth.fido.fido2.common.server.ServerAuthPublicKeyCredential;
import com.linecorp.line.auth.fido.fido2.common.TokenBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Adapter that exposes the existing server ResponseService through the core ResponseService API.
 * It delegates calls to the server-side ResponseService implementation.
 */
@Component
public class CoreResponseServiceAdapter implements ResponseService {

    private final com.linecorp.line.auth.fido.fido2.server.service.ResponseService delegate;

    @Autowired
    public CoreResponseServiceAdapter(com.linecorp.line.auth.fido.fido2.server.service.ResponseService delegate) {
        this.delegate = delegate;
    }

    @Override
    public RegisterCredentialResult handleAttestation(Object serverResponse, String sessionId, String origin, String rpId) {
        ServerRegPublicKeyCredential req = (ServerRegPublicKeyCredential) serverResponse;
        // delegate expects a TokenBinding param; pass null for now
        return delegate.handleAttestation(req, sessionId, origin, rpId, (TokenBinding) null);
    }

    @Override
    public VerifyCredentialResult handleAssertion(Object serverResponse, String sessionId, String origin, String rpId) {
        ServerAuthPublicKeyCredential req = (ServerAuthPublicKeyCredential) serverResponse;
        return delegate.handleAssertion(req, sessionId, origin, rpId, (TokenBinding) null);
    }
}
