package com.mir00r.keycloakdemo.configs

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder


class KeycloakConfig private constructor() {

    companion object {
        @Volatile
        private var keycloak: Keycloak? = null

        const val serverUrl = "http://localhost:8082"
        const val realm = "bankrupt"
        const val clientId = "backend-api"
        const val clientSecret = "WEuxI7SheNzdpqPAUbZiemObQrGt6ZXC"
        const val userName = "admin"
        const val password = "password"

        fun getInstance(): Keycloak {
            if (keycloak == null) {
                synchronized(this) {
                    if (keycloak == null) {
                        keycloak = KeycloakBuilder.builder()
                            .serverUrl(serverUrl)
                            .realm(realm)
                            .grantType(OAuth2Constants.PASSWORD)
                            .username(userName)
                            .password(password)
                            .clientId(clientId)
                            .clientSecret(clientSecret)
                            .resteasyClient(
                                ResteasyClientBuilder()
                                    .connectionPoolSize(10)
                                    .build()
                            )
                            .build()
                    }
                }
            }
            return keycloak!!
        }
    }
}