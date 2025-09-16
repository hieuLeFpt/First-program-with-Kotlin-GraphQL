package com.auth.payload

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class AuthenticationRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Must be a valid email address")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, max = 50, message = "Invalid password!")
    val password: String
)