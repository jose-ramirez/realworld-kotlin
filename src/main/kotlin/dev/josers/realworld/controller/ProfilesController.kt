package dev.josers.realworld.controller

import dev.josers.realworld.RestMethods.Profiles
import dev.josers.realworld.model.User
import dev.josers.realworld.service.ProfileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProfilesController(@Autowired val profileService: ProfileService) {

    @GetMapping(Profiles.V1.PROFILE)
    fun getProfile(@PathVariable username: String) =
        ResponseEntity.ok(profileService.getProfile(username))

    @PostMapping(Profiles.V1.PROFILE_FOLLOW)
    fun followUser(@PathVariable("username") username: String, loggedUser: User) =
        ResponseEntity.ok(profileService.followUser(username, loggedUser))

    @DeleteMapping(Profiles.V1.PROFILE_FOLLOW)
    fun unfollowUser(@PathVariable("username") username: String, loggedUser: User) =
        ResponseEntity.ok(profileService.unfollowUser(username, loggedUser))
}