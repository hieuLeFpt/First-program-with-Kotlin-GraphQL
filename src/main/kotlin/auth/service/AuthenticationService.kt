package com.auth.service

import com.auth.payload.AuthenticationResponse
import com.auth.payload.AuthenticationRequest
import com.auth.payload.RegisterRequest
import com.config.JwtService
import com.entity.Student
import com.exception.StudentDuplicateEmailException
import jakarta.validation.Valid
import com.exception.StudentNotFoundException
import com.repository.RoleRepository
import com.repository.StudentRepository
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@Service
@RequiredArgsConstructor
 class AuthenticationService(
    private val studentRepository: StudentRepository,
    private val passwordEncoder: PasswordEncoder,
    private val roleRepository: RoleRepository,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {
    fun register(request: RegisterRequest): AuthenticationResponse {
        if (studentRepository.existsByEmail(request.email))
            throw StudentDuplicateEmailException("${request.email} is exists!")
        var student = Student(
            name = request.name,
            email = request.email,
            age = request.age,
            major = request.major,
            enrollmentDate = LocalDate.now(),
            role = roleRepository.findByName("USER")
                .orElseThrow { IllegalStateException("Role not found in DB") },
            passwordUser = passwordEncoder.encode(request.password),
        )
        studentRepository.save(student)
        var jwtToken = jwtService.generateToken(student)
        return AuthenticationResponse(
            token = jwtToken,
            roles = student.role?.name
        )
    }

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        // thực chất là ProviderManager .authenticate vì implements AuthenticationManager(interface)
        // mà trong ProviderManager có dùng method .authenticate, AuthenticationProvider(interface) được impl bởi
        // AbstractUserDetailsAuthenticationProvider class đã ghi đè authenticate ở AuthenticationProvider(interface)
        // AbstractUserDetailsAuthenticationProvider có method retrieveUser được
        // DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider và ghi đè retrieveUser và làm nhiệm vụ tìm trong db với
        // tk và mk trong rq
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )
        } catch (ex: BadCredentialsException) {
            println("hieu ne 123")
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai email hoặc mật khẩu")
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi xác thực: ${ex.message}")
        }
        println(authenticationManager::class.qualifiedName)
        var user = studentRepository.findByEmail(request.email)
            .orElseThrow { throw StudentNotFoundException("${request.email} not found!") }
        println(user.toString() + "user tìm trong db")
        var jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(
            token = jwtToken,
            roles = user.role?.name
        )
    }

    fun logOut(){

    }
}