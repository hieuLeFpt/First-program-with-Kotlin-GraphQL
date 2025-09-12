package com.security

import com.config.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
open class SecurityConfig(
) {

    @Bean
    open fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthFilter: JwtAuthenticationFilter,
    ): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { }
            .authorizeHttpRequests { auth ->
                auth
                    // endpoint nào được permitAll thì không cần token
                    .requestMatchers("/graphql").permitAll()
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/error", "/error/**").permitAll()
                    .anyRequest().authenticated()
            }
            //không lưu phiên đăng nhập trên server. Mỗi request đều phải kèm token để xác thực lại.
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            // nếu như không có dòng này thì khi có 1 rq gửi tới dù có token đúng thì vẫn lỗi 403
            // vì jwtAuthFilter đảm nhiệm việc đọc header, validate token, set authen vào SecurityContextHolder
            //SecurityContext là “chỗ chứa” thông tin bảo mật của request hiện tại — quan trọng nhất là đối tượng Authentication (ai đang gọi, đã đăng nhập chưa, có quyền gì).
            //SecurityContext trống nên
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    open fun corsConfigurationSource(): CorsConfigurationSource {
        val c = CorsConfiguration().apply {
            // ĐỔI thành origin FE của bạn. Ví dụ: vue ở 8081
            allowedOrigins = listOf("http://localhost:5173", "http://localhost:8081")
            // hoặc nếu đổi port liên tục: allowedOriginPatterns = listOf("http://localhost:*")
            allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            allowedHeaders = listOf("Authorization", "Content-Type", "X-CSRF-TOKEN")
            exposedHeaders = listOf("Authorization", "Location")
            allowCredentials = true
            maxAge = 3600
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", c)
        }
    }
}
