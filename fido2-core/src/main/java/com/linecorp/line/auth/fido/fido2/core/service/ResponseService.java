package com.linecorp.line.auth.fido.fido2.core.service;

import com.linecorp.line.auth.fido.fido2.common.server.RegisterCredentialResult;
import com.linecorp.line.auth.fido.fido2.common.server.VerifyCredentialResult;

/**
 * Core interface for handling attestation (registration) and assertion (authentication) responses.
 */
public interface ResponseService {
    /**
     * Handle attestation (registration) response.
     * @param serverResponse raw server-side credential/response wrapper (type is left generic to avoid coupling)
     * @param sessionId session id created at challenge time
     * @param origin origin header sent by client
     * @param rpId relying party id
     * @return RegisterCredentialResult containing created credential info
     */
    com.linecorp.line.auth.fido.fido2.common.server.RegisterCredentialResult handleAttestation(Object serverResponse, String sessionId, String origin, String rpId);

    /**
     * Handle assertion (authentication) response.
     * @param serverResponse raw server-side credential/response wrapper
     * @param sessionId session id created at challenge time
     * @param origin origin header sent by client
     * @param rpId relying party id
     * @return VerifyCredentialResult containing verification result
     */
    com.linecorp.line.auth.fido.fido2.common.server.VerifyCredentialResult handleAssertion(Object serverResponse, String sessionId, String origin, String rpId);
}
