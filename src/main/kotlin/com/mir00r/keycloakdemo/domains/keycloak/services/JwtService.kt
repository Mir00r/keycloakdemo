package com.mir00r.keycloakdemo.domains.keycloak.services

import com.auth0.jwk.Jwk
import com.auth0.jwk.JwkProvider
import com.auth0.jwk.UrlJwkProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.net.URL


@Service
class JwtService {

    @Value("\${keycloak.jwk-set-uri}")
    private lateinit var jwksUrl: String

    @Value("\${keycloak.certs-id}")
    private lateinit var certsId: String

    @Cacheable(value = ["jwkCache"])
    @Throws(Exception::class)
    fun getJwk(): Jwk {
        val jwkProvider: JwkProvider = UrlJwkProvider(URL(jwksUrl))
        return jwkProvider.get(certsId)
    }
}
