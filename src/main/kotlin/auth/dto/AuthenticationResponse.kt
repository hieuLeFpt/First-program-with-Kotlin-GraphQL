package com.auth.payload

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
data class AuthenticationResponse(
    var token: String? = null,
    var roles: String? = null
)