package dev.josers.realworld.service

import dev.josers.realworld.config.JWTUtils
import dev.josers.realworld.config.updateVal
import dev.josers.realworld.model.User
import dev.josers.realworld.repository.UserRepository
import dev.josers.realworld.request.UserLoginRequestVO
import dev.josers.realworld.request.UserRequestVO
import dev.josers.realworld.response.UserResponseVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(@Autowired val userRepository: UserRepository,
                  @Autowired val encoder: BCryptPasswordEncoder,
                  @Autowired val jwtUtils: JWTUtils) {

    fun registerUser(data: UserRequestVO): UserResponseVO {
        val userData = data.user!!
        userData.password = encoder.encode(userData.password)
        val savedUser = userRepository.save(userData.toUser())
        return UserResponseVO(
            user = UserResponseVO.UserDataResponse(
                email = savedUser.email,
                username = savedUser.username,
                bio = savedUser.bio,
                image = savedUser.image,
                token = jwtUtils.doGenerateToken(HashMap(), savedUser.email)
            )
        )
    }

    fun loginUser(data: UserLoginRequestVO): UserResponseVO {
        val userData = data.user!!
        val possibleUser = userRepository.findByEmail(userData.email)
        return if (possibleUser != null) {
            if(encoder.matches(userData.password, possibleUser.password)){
                UserResponseVO(
                    user = UserResponseVO.UserDataResponse(
                        email = possibleUser.email,
                        username = possibleUser.username,
                        bio = possibleUser.bio,
                        image = possibleUser.image,
                        token = jwtUtils.doGenerateToken(HashMap(), possibleUser.email)
                    )
                )
            } else UserResponseVO(user = null)
        } else UserResponseVO(user = null)
    }

    fun getUser(request: UserRequestVO, loggedUser: User): UserResponseVO {
        val possibleUser = userRepository.findByEmail(loggedUser.email)
        return if(possibleUser != null){
            UserResponseVO(
                    user = UserResponseVO.UserDataResponse(
                            email = possibleUser.email,
                            username = possibleUser.username,
                            bio = possibleUser.bio,
                            image = possibleUser.image,
                            token = jwtUtils.doGenerateToken(HashMap(), possibleUser.email)
                    )
            )
        } else UserResponseVO(user = null)
    }

    fun updateUser(request: UserRequestVO, loggedUser: User): UserResponseVO {

        loggedUser.email = loggedUser.email.updateVal(request.user?.email)
        loggedUser.password = loggedUser.password.updateVal(request.user?.password)
        loggedUser.username = loggedUser.username.updateVal(request.user?.username)
        loggedUser.bio = loggedUser.bio?.updateVal(request.user?.bio)
        loggedUser.image = loggedUser.image?.updateVal(request.user?.image)

        userRepository.save(loggedUser)

        return UserResponseVO(
            user = UserResponseVO.UserDataResponse(
                email = loggedUser.email,
                username = loggedUser.username,
                bio = loggedUser.bio,
                image = loggedUser.image,
                token = jwtUtils.doGenerateToken(HashMap(), loggedUser.email)
            )
        )
    }
}