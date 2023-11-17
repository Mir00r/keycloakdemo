package com.mir00r.keycloakdemo.domains.keycloak.controllers

import com.mir00r.keycloakdemo.domains.keycloak.models.UserDto
import com.mir00r.keycloakdemo.domains.keycloak.services.KeycloakUserService
import com.mir00r.keycloakdemo.routing.Route
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
class KeycloakUserController @Autowired constructor(
    private val keycloakUserService: KeycloakUserService
) {

    @PostMapping(Route.V1.CREATE_USERS)
    fun addUser(@RequestBody userDTO: UserDto): String {
        keycloakUserService.addUser(userDTO)
        return "User Added Successfully."
    }

    @GetMapping(Route.V1.FIND_USERS_BY_USERNAME)
    fun getUser(@PathVariable("userName") userName: String?): List<UserRepresentation> {
        return keycloakUserService.getUser(userName)
    }

    @PutMapping(Route.V1.UPDATE_USERS)
    fun updateUser(@PathVariable("id") userId: String?, @RequestBody userDTO: UserDto): String {
        keycloakUserService.updateUser(userId, userDTO)
        return "User Details Updated Successfully."
    }

    @DeleteMapping(Route.V1.DELETE_USERS)
    fun deleteUser(@PathVariable("id") userId: String?): String {
        keycloakUserService.deleteUser(userId)
        return "User Deleted Successfully."
    }

    @GetMapping(Route.V1.SEND_VERIFICATION_LINK_BY_ID)
    fun sendVerificationLink(@PathVariable("id") userId: String?): String {
        keycloakUserService.sendVerificationLink(userId)
        return "Verification Link Send to Registered E-mail Id."
    }

    @GetMapping(Route.V1.RESET_PASSWORD_BY_ID)
    fun sendResetPassword(@PathVariable("id") userId: String?): String {
        keycloakUserService.sendResetPassword(userId)
        return "Reset Password Link Send Successfully to Registered E-mail Id."
    }
}