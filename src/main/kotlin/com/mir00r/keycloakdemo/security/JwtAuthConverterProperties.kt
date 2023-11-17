package com.mir00r.keycloakdemo.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "jwt.auth.converter")
class JwtAuthConverterProperties {

    var resourceId: String? = null
    var principalAttribute: String? = null
}