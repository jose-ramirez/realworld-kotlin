package dev.josers.realworld.config

import dev.josers.realworld.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.server.ResponseStatusException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JWTAuthFilter(@Autowired val jwtUtils: JWTUtils,
                    @Autowired @Qualifier("myService") val service: UserDetailsService): OncePerRequestFilter() {

    private val AUTH_HEADER = "Authorization"
    private val TOKEN_PREFIX = "Token"

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = getToken(request)

        if(token != null) {
            try {
                val email = jwtUtils.getAllClaimsFromToken(token).subject

                try{
                    val userDetails = service.loadUserByUsername(email)
                    val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                } catch (ex: UsernameNotFoundException) {
                    throw ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid token", ex)
                }

            } catch (e: Exception) {
                // do something here, like logging
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun getToken(request: HttpServletRequest): String? {
        val tokenHeader = request.getHeader(AUTH_HEADER)
        return if(tokenHeader != null && tokenHeader.startsWith(TOKEN_PREFIX)){
            tokenHeader.split(" ")[1]
        } else null
    }
}
