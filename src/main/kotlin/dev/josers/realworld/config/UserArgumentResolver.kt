package dev.josers.realworld.config

import dev.josers.realworld.model.User
import dev.josers.realworld.repository.UserRepository
import dev.josers.realworld.utils.JWTUtils
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class UserArgumentResolver(private val userRepository: UserRepository, private val jwtUtils: JWTUtils): HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameter.type == User::class.java
    }

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): User? {

        val token = webRequest.getHeader("Authorization")?.split(" ")?.get(1)
        val email = jwtUtils.getAllClaimsFromToken(token).subject
        return userRepository.findByEmail(email)
    }
}