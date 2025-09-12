package com.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.lang.NonNull
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.ServletException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import java.io.IOException
import org.springframework.security.core.userdetails.UserDetailsService


@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailService: UserDetailsService
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        @NonNull request: HttpServletRequest,
        @NonNull response: HttpServletResponse,
        @NonNull filterChain: FilterChain
    ) {
        val authHeader: String? = request.getHeader("Authorization")
        val userEmail: String
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            println("chưa đăng nhập")
            filterChain.doFilter(request, response)
            return
        }
        val jwt: String = authHeader.substring("Bearer ".length)
        println(jwt + "token bắt được là")
        //bóc email từ token để đi xác thực người dùng
        userEmail = jwtService.extractUserName(jwt)
        println("$userEmail email bắt được là")
        if (SecurityContextHolder.getContext().authentication == null) {
            // tìm email được bóc từ token trong db
            val userDetail: UserDetails = this.userDetailService.loadUserByUsername(userEmail)
            println(userDetail.toString() + "userDetail")
            if (jwtService.isTokenValid(jwt, userDetail)) {
                println("valid")
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetail,
                    null,
                    userDetail.authorities
                )
                println(authToken)

                // để lưu lại địa chỉ ip và sessionId của rq
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        filterChain.doFilter(request, response)
    }
}