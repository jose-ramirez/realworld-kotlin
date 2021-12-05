package dev.josers.realworld.model

data class Profile (
    var idUser: String,
    var username: String,
    var bio: String?,
    var image: String?,
    var following: Boolean)