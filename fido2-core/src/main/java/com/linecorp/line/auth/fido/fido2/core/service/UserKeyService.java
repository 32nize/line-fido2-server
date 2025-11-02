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

import java.util.List;

/**
 * Service for managing user key (credential) registrations.
 */
public interface UserKeyService {
    /**
     * Check if a user has any registered keys for an RP.
     *
     * @param rpId The Relying Party ID
     * @param userId The user ID
     * @return true if the user has registered keys
     */
    boolean hasRegistrations(String rpId, String userId);

    /**
     * Check if a credential ID is registered.
     *
     * @param rpId The Relying Party ID
     * @param credentialId The credential ID
     * @return true if the credential exists
     */
    boolean hasCredential(String rpId, String credentialId);

    /**
     * Get user keys by user ID.
     *
     * @param rpId The Relying Party ID
     * @param userId The user ID
     * @return List of user keys
     */
    List<Object> getUserKeys(String rpId, String userId);

    /**
     * Get user key by credential ID.
     *
     * @param rpId The Relying Party ID
     * @param credentialId The credential ID
     * @return The user key or null if not found
     */
    Object getUserKey(String rpId, String credentialId);
}