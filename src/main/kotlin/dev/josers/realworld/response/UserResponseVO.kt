package dev.josers.realworld.response

class UserResponseVO(val user: UserDataResponse? = null) {
    data class UserDataResponse(val email: String = "",
                                val username: String = "",
                                val bio: String? = "",
                                val image: String? = "",
                                val token: String = "")
}