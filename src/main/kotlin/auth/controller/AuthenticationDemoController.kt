package com.auth.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/demo")
class AuthenticationDemoController {
    @GetMapping
    fun authenticationDemo(): ResponseEntity<String> {
        println("debug")
        return ResponseEntity("Authentication Demo", HttpStatus.OK)
    }
}