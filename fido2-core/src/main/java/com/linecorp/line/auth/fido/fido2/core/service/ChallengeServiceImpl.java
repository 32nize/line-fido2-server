/*
 * Copyright 2024 LY Corporation
 *
 * LY Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.line.auth.fido.fido2.core.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.linecorp.line.auth.fido.fido2.common.AttestationConveyancePreference;
import com.linecorp.line.auth.fido.fido2.common.AuthenticatorAttachment;
import com.linecorp.line.auth.fido2.server.model.AuthenticatorSelection;
import com.linecorp.line.auth.fido.fido2.common.PublicKeyCredentialDescriptor;
import com.linecorp.line.auth.fido.fido2.common.PublicKeyCredentialParameters;
import com.linecorp.line.auth.fido.fido2.common.PublicKeyCredentialRpEntity;
import com.linecorp.line.auth.fido.fido2.common.PublicKeyCredentialType;
import com.linecorp.line.auth.fido.fido2.common.PublicKeyCredentialUserEntity;
import com.linecorp.line.auth.fido.fido2.common.ResidentKeyRequirement;
import com.linecorp.line.auth.fido.fido2.common.UserVerificationRequirement;
import com.linecorp.line.auth.fido.fido2.common.server.AuthOptionRequest;
import com.linecorp.line.auth.fido.fido2.common.server.AuthOptionResponse;
import com.linecorp.line.auth.fido.fido2.common.server.RegOptionRequest;
import com.linecorp.line.auth.fido.fido2.common.server.RegOptionResponse;
import com.linecorp.line.auth.fido.fido2.server.ServerConstant;
import com.linecorp.line.auth.fido.fido2.server.error.InternalErrorCode;
import com.linecorp.line.auth.fido.fido2.server.exception.FIDO2ServerRuntimeException;
import com.linecorp.line.auth.fido.fido2.server.util.ChallengeGenerator;

import lombok.extern.slf4j.Slf4j;

/**
 * Core implementation of the ChallengeService interface.
 */
@Slf4j
public class ChallengeServiceImpl implements ChallengeService {

    private final RelyingPartyService rpService;
    private final UserKeyService userKeyService;
    private final SessionService sessionService;
    private final Duration sessionTimeout;

    public ChallengeServiceImpl(RelyingPartyService rpService,
                               UserKeyService userKeyService,
                               SessionService sessionService,
                               Duration sessionTimeout) {
        this.rpService = rpService;
        this.userKeyService = userKeyService;
        this.sessionService = sessionService;
        this.sessionTimeout = sessionTimeout;
    }

    @Override
    public RegOptionResponse getRegChallenge(RegOptionRequest regOptionRequest) {
        String rpId = regOptionRequest.getRpId();
        String userId = regOptionRequest.getUserId();
        log.debug("ChallengeService::getRegChallenge: rpId={}, userId={}", rpId, userId);

        RegOptionResponse.RegOptionResponseBuilder builder = RegOptionResponse.builder();

        // Check if RP exists
        if (!rpService.exists(rpId)) {
            throw new FIDO2ServerRuntimeException(InternalErrorCode.RPID_NOT_FOUND, "RP ID not found: " + rpId);
        }

        // Set RP info
        PublicKeyCredentialRpEntity rp = rpService.get(rpId);
        builder.rp(rp);

        // Set user info
        PublicKeyCredentialUserEntity user = new PublicKeyCredentialUserEntity();
        user.setId(userId);
        user.setName(regOptionRequest.getUserName());
        user.setDisplayName(regOptionRequest.getUserDisplayName());
        builder.user(user);

        // Set challenge
        builder.challenge(ChallengeGenerator.generate(ServerConstant.SERVER_CHALLENGE_LENGTH));

        // Set pubKeyCredParams
        List<PublicKeyCredentialParameters> pubKeyCredParams = new ArrayList<>();
        pubKeyCredParams.add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, -7)); // ES256
        pubKeyCredParams.add(new PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, -257)); // RS256
        builder.pubKeyCredParams(pubKeyCredParams);

        // Set timeout
        builder.timeout(sessionTimeout.toMillis());

        // Set authenticator selection
        AuthenticatorSelection authenticatorSelection = new AuthenticatorSelection();
        authenticatorSelection.setAuthenticatorAttachment(AuthenticatorAttachment.CROSS_PLATFORM);
        authenticatorSelection.setRequireResidentKey(true);
        authenticatorSelection.setResidentKey(ResidentKeyRequirement.REQUIRED);
        authenticatorSelection.setUserVerification(UserVerificationRequirement.PREFERRED);
        builder.authenticatorSelection(authenticatorSelection);

        // Set attestation
        builder.attestation(AttestationConveyancePreference.DIRECT);

        RegOptionResponse response = builder.build();

        // Store session
        String sessionId = sessionService.createRegistrationSession(response, sessionTimeout);
        response.setSessionId(sessionId);

        return response;
    }

    @Override
    public AuthOptionResponse getAuthChallenge(AuthOptionRequest authOptionRequest) {
        String rpId = authOptionRequest.getRpId();
        String userId = authOptionRequest.getUserId();
        log.debug("ChallengeService::getAuthChallenge: rpId={}, userId={}", rpId, userId);

        AuthOptionResponse.AuthOptionResponseBuilder builder = AuthOptionResponse.builder();

        // Check if RP exists
        if (!rpService.exists(rpId)) {
            throw new FIDO2ServerRuntimeException(InternalErrorCode.RPID_NOT_FOUND, "RP ID not found: " + rpId);
        }

        // Create challenge
        builder.challenge(ChallengeGenerator.generate(ServerConstant.SERVER_CHALLENGE_LENGTH));

        // Set timeout
        builder.timeout(sessionTimeout.toMillis());

        // Set user verification
        builder.userVerification(UserVerificationRequirement.PREFERRED);

        // Set allowed credentials if user ID is provided
        if (userId != null && !userId.isEmpty()) {
            List<Object> userKeys = userKeyService.getUserKeys(rpId, userId);
            if (userKeys.isEmpty()) {
                throw new FIDO2ServerRuntimeException(InternalErrorCode.USER_NOT_FOUND, 
                    "No credentials found for user: " + userId);
            }

            List<PublicKeyCredentialDescriptor> allowCredentials = new ArrayList<>();
            // Convert user keys to credential descriptors
            // Implementation depends on UserKey model
            // TODO: Add conversion logic

            builder.allowCredentials(allowCredentials);
        }

        // Set RP ID
        builder.rpId(rpId);

        AuthOptionResponse response = builder.build();

        // Store session
        String sessionId = sessionService.createAuthenticationSession(response, sessionTimeout);
        response.setSessionId(sessionId);

        return response;
    }
}