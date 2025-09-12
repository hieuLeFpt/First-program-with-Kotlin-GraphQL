package com.config

import com.repository.StudentRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
open class ApplicationConfig(
    private val studentRepository: StudentRepository,
) {

    //vì UserDetailsService là functional interface chỉ có 1 method nên
    // có thể viết đè bằng lambda
    @Bean
    open fun userDetailsService(): UserDetailsService {
        return UserDetailsService { email ->
            studentRepository.findByEmail(email)
                .orElseThrow { UsernameNotFoundException("User not found with email $email") }
        }
    }

    //ProviderManager
    @Bean
    open fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    @Bean
    open fun passwordEncoder() = BCryptPasswordEncoder()

}