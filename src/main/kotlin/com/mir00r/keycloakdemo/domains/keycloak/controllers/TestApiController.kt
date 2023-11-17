package com.mir00r.keycloakdemo.domains.keycloak.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/test")
class TestApiController {

    @GetMapping("/hello-1")
    fun hello1(): String {
        return "Anyone can access"
    }

    @GetMapping("/hello-2")
    fun hello2(): String {
        return "ADMIN can access"
    }

    @GetMapping("/hello-3")
    fun hello3(): String {
        return "USER can access"
    }

    @GetMapping("/hello-4")
    fun hello4(): String {
        return "ADMIN/ USER can access"
    }
}