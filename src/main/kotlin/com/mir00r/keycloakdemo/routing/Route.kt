package com.mir00r.keycloakdemo.routing

class Route {
    class V1 {
        companion object {
            private const val API = "/api"
            private const val VERSION = "/v1"
            private const val ADMIN = "/admin"

            // Users API's
            const val SEARCH_USERS = "$API$VERSION/users"
            const val CREATE_USERS = "$API$VERSION/users"
            const val FIND_USERS = "$API$VERSION/users/{id}"
            const val UPDATE_USERS = "$API$VERSION/users/{id}"
            const val DELETE_USERS = "$API$VERSION/users/{id}"
            const val FIND_USERS_BY_USERNAME = "$API$VERSION/users/{userName}"
            const val SEND_VERIFICATION_LINK_BY_ID = "$API$VERSION/users/verification-link/{id}"
            const val RESET_PASSWORD_BY_ID = "$API$VERSION/users/reset-password/{id}"

        }
    }
}
