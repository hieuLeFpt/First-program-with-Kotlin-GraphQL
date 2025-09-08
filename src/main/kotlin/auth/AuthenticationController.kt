package com.auth

import com.auth.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequest
    ): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.register(request))
    }

    @PostMapping("/authenticate")
    fun authenticate(
        @RequestBody request: AuthenticationRequest
    ): ResponseEntity<AuthenticationResponse> {
        println("vào được controller")
        return ResponseEntity.ok(authenticationService.authenticate(request))
    }
}

