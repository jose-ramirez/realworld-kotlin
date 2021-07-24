package dev.josers.realworld.service

import dev.josers.realworld.model.UserDetailsImpl
import dev.josers.realworld.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service("myService")
class UserDetailsServiceImpl @Autowired constructor(private val userRepository: UserRepository): UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails{
        val user = userRepository.findByEmail(email)
        return if (user != null) {
            UserDetailsImpl(user)
        } else throw UsernameNotFoundException("User not found")
    }
}