package dev.josers.realworld.vo.request

class UserLoginRequestVO(var user: Credentials? = null) {
    data class Credentials(val email: String = "", val password: String = "")
}