package dev.josers.realworld.config

import dev.josers.realworld.repository.UserRepository
import dev.josers.realworld.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class UserArgumentResolverConfiguration(@Autowired val userRepository: UserRepository,
                                        @Autowired val jwtUtils: JWTUtils): WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(UserArgumentResolver(userRepository, jwtUtils))
    }
}