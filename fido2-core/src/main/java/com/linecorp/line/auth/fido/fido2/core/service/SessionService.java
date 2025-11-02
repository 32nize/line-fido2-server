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
import com.linecorp.line.auth.fido.fido2.common.server.RegOptionResponse;
import com.linecorp.line.auth.fido.fido2.common.server.AuthOptionResponse;

/**
 * Service for managing WebAuthn/FIDO2 sessions.
 */
public interface SessionService {
    /**
     * Create a new registration session.
     *
     * @param response The registration options response
     * @param timeout Session timeout duration
     * @return The created session ID
     */
    String createRegistrationSession(RegOptionResponse response, Duration timeout);

    /**
     * Create a new authentication session.
     *
     * @param response The authentication options response
     * @param timeout Session timeout duration
     * @return The created session ID
     */
    String createAuthenticationSession(AuthOptionResponse response, Duration timeout);

    /**
     * Find a session by ID.
     *
     * @param sessionId The session ID to find
     * @return The found session, or null if not found
     */
    Object findSession(String sessionId);

    /**
     * Delete a session by ID.
     *
     * @param sessionId The session ID to delete
     */
    void deleteSession(String sessionId);
}