package com.config

import org.springframework.stereotype.Service
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import java.util.Date
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import java.security.Key

@Service
class JwtService(
    @Value("\${jwt.secret-key}") private val secretKey: String
) {
    fun extractUserName(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    private fun extractExpireDate(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpireDate(token).before(Date())
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val email = extractUserName(token)
        return (email.equals(userDetails.username)) && !isTokenExpired(token)
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(emptyMap(), userDetails)
    }

    private fun generateToken(
        extraClaims: Map<String, Any>,
        userDetail: UserDetails
    ): String {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            //ghi đè key tương ứng trong claims
            .setSubject(userDetail.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 24))// sống 24p
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    // tách token ra thành 3 phần Header.Payload.Signature
    private fun extractAllClaims(token: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
    }

    fun getSignInKey(): Key {
        // lấy chuỗi secretKey trong application để chuyển thành mảng byte
        val keyBytes: ByteArray = Decoders.BASE64.decode(secretKey)
        // trả về 1 SecretKey mới có thể đưa vào .signWith() để ký token hoặc .setSigningKey() để verify token.
        return Keys.hmacShaKeyFor(keyBytes)
    }
}