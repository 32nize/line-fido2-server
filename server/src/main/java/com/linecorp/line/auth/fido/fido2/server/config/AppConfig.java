package com.linecorp.line.auth.fido.fido2.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "com.linecorp.line.auth.fido")
@EnableJpaRepositories(basePackages = "com.linecorp.line.auth.fido.fido2.server.repository")
public class AppConfig {
  
}
