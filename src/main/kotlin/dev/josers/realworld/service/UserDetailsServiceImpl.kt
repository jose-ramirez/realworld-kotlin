package dev.josers.realworld.service

import dev.josers.realworld.model.UserDetailsImpl
import dev.josers.realworld.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service("myService")
class UserDetailsServiceImpl(@Autowired val userRepository: UserRepository): UserDetailsService {

    override fun loadUserByUsername(email: String) =
            UserDetailsImpl(userRepository.findByEmail(email))
}