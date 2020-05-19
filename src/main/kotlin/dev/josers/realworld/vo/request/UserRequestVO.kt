package dev.josers.realworld.vo.request

import dev.josers.realworld.model.User

data class UserRequestVO(var user: UserData? = null) {
    data class UserData (val email: String = "",
                    var password: String = "",
                    val username: String = "",
                    val bio: String? = "",
                    val image: String? = "") {

        fun toUser() = User(
                email = this.email,
                password = this.password,
                username = this.username,
                bio = this.bio,
                image = this.image
        )
    }
}