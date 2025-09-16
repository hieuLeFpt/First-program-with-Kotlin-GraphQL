package com.dto

import jakarta.validation.constraints.*

data class StudentCreateDTO (
    val id: Int? = null,

    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    var name: String? = null,

    @field:NotNull(message = "Age is required")
    @field:Min(value = 18, message = "Age must be at least 18")
    @field:Max(value = 100, message = "Age must be less than or equal to 100")
    var age: Int? = null,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Must be a valid email address")
    var email: String? = null,

    @field:NotBlank(message = "Major is required")
    var major: String? = null,

    @field:NotNull(message = "Password is required")
    @field:Size(min = 6, max = 50, message = "Invalid password!")
    var passwordUser: String? = null,
)
