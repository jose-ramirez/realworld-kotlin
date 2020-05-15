package dev.josers.realworld.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JWTUtils {
    @Value("\${jwt.secret}")
    lateinit var secret: String

    fun getAllClaimsFromToken(token: String?) =
        Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body

    //while creating the token -
    //1. Define claims of the token, like Issuer, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    fun doGenerateToken(claims: Map<String, Any>, subject: String) =
        Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact()
}