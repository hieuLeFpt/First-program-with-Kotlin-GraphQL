package com.auth.controller

import com.auth.payload.AuthenticationRequest
import com.auth.payload.AuthenticationResponse
import com.auth.service.AuthenticationService
import com.auth.payload.RegisterRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.validation.BindingResult

@RestController
@RequestMapping("/api/auth")
open class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequest
    ): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.register(request))
    }

    @PostMapping("/authenticate")
    open fun authenticate(
        @Valid
        @RequestBody request: AuthenticationRequest,
    ): ResponseEntity<AuthenticationResponse> {
        println("vào được controller")
        return ResponseEntity.ok(authenticationService.authenticate(request))
    }

}