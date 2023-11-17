package com.mir00r.keycloakdemo.domains.keycloak.services

import com.mir00r.keycloakdemo.configs.KeycloakConfig
import com.mir00r.keycloakdemo.domains.keycloak.models.UserDto
import com.mir00r.keycloakdemo.utils.Credentials
import org.keycloak.admin.client.resource.UsersResource
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Service


@Service
class KeycloakUserService {

    fun addUser(dto: UserDto) {
        val credential: CredentialRepresentation = Credentials.createPasswordCredentials(dto.password)

        val user = UserRepresentation()
        user.username = dto.userName
        user.firstName = dto.firstname
        user.lastName = dto.lastName
        user.email = dto.emailId
        user.credentials = listOf(credential)
        user.isEnabled = true
        val instance = getInstance()
        instance.create(user)
    }

    fun getUser(userName: String?): List<UserRepresentation> {
        val usersResource = getInstance()
        return usersResource.search(userName, true)
    }

    fun updateUser(userId: String?, dto: UserDto) {
        val credential: CredentialRepresentation = Credentials.createPasswordCredentials(dto.password)

        val user = UserRepresentation()
        user.username = dto.userName
        user.firstName = dto.firstname
        user.lastName = dto.lastName
        user.email = dto.emailId
        user.credentials = listOf(credential)
        val usersResource = getInstance()
        usersResource[userId].update(user)
    }

    fun deleteUser(userId: String?) {
        val usersResource = getInstance()
        usersResource[userId]
            .remove()
    }

    fun sendVerificationLink(userId: String?) {
        val usersResource = getInstance()
        usersResource[userId]
            .sendVerifyEmail()
    }

    fun sendResetPassword(userId: String?) {
        val usersResource = getInstance()
        usersResource[userId].executeActionsEmail(mutableListOf("UPDATE_PASSWORD"))
    }

    fun getInstance(): UsersResource {
        return KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users()
    }

}