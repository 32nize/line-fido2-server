/*
 * Copyright 2021 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
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

package com.linecorp.line.auth.fido.fido2.rpserver.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@Profile("!prod")
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("FIDO2 SERVER REST API")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public Info metaData() {
        return new Info()
                .title("FIDO2 RP SERVER REST API")
                .description("If you want to know the details of the WebAuthn standard, you can also refer to the official documentation. \n\n https://www.w3.org/TR/webauthn-2/" )
                .version("1.0.0")
                .contact(new Contact().name("Kyung-Joon Park").email("kyungjoon.park@linecorp.com"));
    }
}
