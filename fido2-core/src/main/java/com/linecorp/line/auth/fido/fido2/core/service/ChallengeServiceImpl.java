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

import com.linecorp.line.auth.fido.fido2.common.server.RegOptionRequest;
import com.linecorp.line.auth.fido.fido2.common.server.RegOptionResponse;
import com.linecorp.line.auth.fido.fido2.common.server.AuthOptionRequest;
import com.linecorp.line.auth.fido.fido2.common.server.AuthOptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Adapter implementation for ChallengeService in core module.
 * Delegates to the existing server-side ChallengeService implementation.
 */
@Component
public class ChallengeServiceImpl implements ChallengeService {

    private final com.linecorp.line.auth.fido.fido2.server.service.ChallengeService delegate;

    @Autowired
    public ChallengeServiceImpl(com.linecorp.line.auth.fido.fido2.server.service.ChallengeService delegate) {
        this.delegate = delegate;
    }

    @Override
    public RegOptionResponse getRegChallenge(RegOptionRequest regOptionRequest) {
        return delegate.getRegChallenge(regOptionRequest);
    }

    @Override
    public AuthOptionResponse getAuthChallenge(AuthOptionRequest authOptionRequest) {
        return delegate.getAuthChallenge(authOptionRequest);
    }
}