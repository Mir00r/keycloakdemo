package com.mir00r.keycloakdemo.domains.keycloak.controllers

import com.auth0.jwk.Jwk
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.mir00r.keycloakdemo.domains.keycloak.services.JwtService
import com.mir00r.keycloakdemo.domains.keycloak.services.KeycloakRestService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.security.interfaces.RSAPublicKey
import java.util.*

@RestController
class KeycloakTestController @Autowired constructor(
    private val restService: KeycloakRestService,
    private val jwtService: JwtService
) {

    private val logger: Logger = LoggerFactory.getLogger(KeycloakTestController::class.java)

    @GetMapping("/student")
    fun student(@RequestHeader("Authorization") authHeader: String): HashMap<*, *> {
        return try {
            val roles: List<String> = restService.getRoles(authHeader)
            if (!roles.contains("student")) {
                throw Exception("not a student role")
            }
            hashMapOf("role" to "student")
        } catch (e: Exception) {
            logger.error("exception : {} ", e.message)
            hashMapOf("status" to "forbidden")
        }
    }

    @GetMapping("/teacher")
    fun teacher(@RequestHeader("Authorization") authHeader: String): HashMap<*, *> {
        return try {
            val jwt: DecodedJWT = JWT.decode(authHeader.replace("Bearer", "").trim())

            // Check JWT is valid
            val jwk: Jwk = jwtService.getJwk()
            val algorithm: Algorithm = Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null)

            algorithm.verify(jwt)

            // Check JWT role is correct
            val roles: List<String> = (jwt.getClaim("realm_access").asMap()["roles"] as List<*>).filterIsInstance<String>()
            if (!roles.contains("teacher")) {
                throw Exception("not a teacher role")
            }

            // Check JWT is still active
            val expiryDate: Date = jwt.expiresAt
            if (expiryDate.before(Date())) {
                throw Exception("token is expired")
            }

            // All validation passed
            hashMapOf("role" to "teacher")
        } catch (e: Exception) {
            logger.error("exception : {} ", e.message)
            hashMapOf("status" to "forbidden")
        }
    }

    @GetMapping("/valid")
    fun valid(@RequestHeader("Authorization") authHeader: String): HashMap<*, *> {
        return try {
            restService.checkValidity(authHeader)
            hashMapOf("is_valid" to "true")
        } catch (e: Exception) {
            logger.error("token is not valid, exception : {} ", e.message)
            hashMapOf("is_valid" to "false")
        }
    }


    @PostMapping("/login")
    fun login(@RequestParam("username") username: String, @RequestParam("password") password: String): String {
        return restService.login(username, password)
    }

    @PostMapping(value = ["/logout"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun logout(@RequestParam(value = "refresh_token", name = "refresh_token") refreshToken: String): Map<*, *> {
        return try {
            restService.logout(refreshToken)
            hashMapOf("logout" to "true")
        } catch (e: Exception) {
            logger.error("unable to logout, exception : {} ", e.message)
            hashMapOf("logout" to "false")
        }
    }

    @RequestMapping("/")
    fun index(): HashMap<*, *> {
        return hashMapOf("status" to "hello world")
    }
}
