package dev.josers.realworld.request

import dev.josers.realworld.model.User

class UserLoginRequestVO(var user: Credentials? = null) {
    data class Credentials(val email: String = "", val password: String = "") {

        fun toUser() = User(
                id = "",
                email = this.email,
                password = this.password,
                bio = null,
                image = null,
                username = ""
        )
    }
}