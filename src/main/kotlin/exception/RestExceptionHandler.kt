package com.exception

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class RestExceptionHandler {

    data class ApiError(
        val status: Int,
        val error: String,
        val message: String,
        val fields: Map<String, String>? = null,
        val timestamp: Instant = Instant.now()
    )

    // @Valid trên @RequestBody và @ModelAttribute
    @ExceptionHandler(value = [MethodArgumentNotValidException::class, BindException::class])
    fun handleInvalidBody(ex: MethodArgumentNotValidException): ResponseEntity<ApiError> {
        val fields = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid") }
        return ResponseEntity.badRequest().body(
            ApiError(
                400,
                "Bad Request",
                "Validation failed",
                fields
            )
        )
    }

    // 401: sai tài khoản/mật khẩu
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCred(): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ApiError(401,
                "Unauthorized",
                "Sai email hoặc mật khẩu")
        )

}
