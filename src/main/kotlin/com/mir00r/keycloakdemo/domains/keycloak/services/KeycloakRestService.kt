package com.mir00r.keycloakdemo.domains.keycloak.services

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@Service
class KeycloakRestService(private val restTemplate: RestTemplate) {

    @Value("\${keycloak.token-uri}")
    private lateinit var keycloakTokenUri: String

    @Value("\${keycloak.user-info-uri}")
    private lateinit var keycloakUserInfo: String

    @Value("\${keycloak.logout}")
    private lateinit var keycloakLogout: String

    @Value("\${keycloak.client-id}")
    private lateinit var clientId: String

    @Value("\${keycloak.authorization-grant-type}")
    private lateinit var grantType: String

    @Value("\${keycloak.client-secret}")
    private lateinit var clientSecret: String

    @Value("\${keycloak.scope}")
    private lateinit var scope: String

    fun login(username: String, password: String): String {
        val map: MultiValueMap<String, String> = LinkedMultiValueMap()
        map.add("username", username)
        map.add("password", password)
        map.add("client_id", clientId)
        map.add("grant_type", grantType)
        map.add("client_secret", clientSecret)

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val request: HttpEntity<MultiValueMap<String, String>> = HttpEntity(map, headers)
        return restTemplate.postForObject(keycloakTokenUri, request, String::class.java) ?: ""
    }

    @Throws(Exception::class)
    fun checkValidity(token: String): String {
        return getUserInfo(token)
    }

    @Throws(Exception::class)
    fun logout(refreshToken: String) {
        val map: MultiValueMap<String, String> = LinkedMultiValueMap()
        map.add("client_id", clientId)
        map.add("client_secret", clientSecret)
        map.add("refresh_token", refreshToken)

        val request: HttpEntity<MultiValueMap<String, String>> = HttpEntity(map, null)
        restTemplate.postForObject(keycloakLogout, request, String::class.java)
    }

    @Throws(Exception::class)
    fun getRoles(token: String): List<String> {
        val response = getUserInfo(token)

        // Get roles
        val map: Map<*, *> = ObjectMapper().readValue(response, HashMap::class.java)
        return map["roles"] as List<String>
    }

    private fun getUserInfo(token: String): String {
        val headers = HttpHeaders()
        headers.add("Authorization", token)

        val request: HttpEntity<*> = HttpEntity(null, headers)
        return restTemplate.postForObject(keycloakUserInfo, request, String::class.java) ?: ""
    }
}
