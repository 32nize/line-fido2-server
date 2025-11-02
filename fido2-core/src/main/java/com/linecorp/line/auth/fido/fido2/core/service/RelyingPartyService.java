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
import com.linecorp.line.auth.fido.fido2.common.PublicKeyCredentialRpEntity;

/**
 * Service for managing Relying Party (RP) information.
 */
public interface RelyingPartyService {
    /**
     * Check if a Relying Party ID exists.
     *
     * @param rpId The Relying Party ID
     * @return true if the RP exists
     */
    boolean exists(String rpId);

    /**
     * Get Relying Party information by ID.
     *
     * @param rpId The Relying Party ID
     * @return The RP entity or null if not found
     */
    PublicKeyCredentialRpEntity get(String rpId);

    /**
     * Get all registered Relying Parties.
     *
     * @return List of all RP entities
     */
    List<PublicKeyCredentialRpEntity> getAll();
}