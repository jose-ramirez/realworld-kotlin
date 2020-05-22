package dev.josers.realworld.controller

import dev.josers.realworld.model.User
import dev.josers.realworld.service.ProfileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProfilesController(@Autowired val profileService: ProfileService) {

    @GetMapping("api/profiles/{username}")
    fun getProfile(@PathVariable username: String) =
        ResponseEntity.ok(profileService.getProfile(username))

    @PostMapping("/api/profiles/{username}/follow")
    fun followUser(@PathVariable("username") username: String, loggedUser: User) =
        ResponseEntity.ok(profileService.followUser(username, loggedUser))

    @DeleteMapping("/api/profiles/{username}/follow")
    fun unfollowUser(@PathVariable("username") username: String, loggedUser: User) =
        ResponseEntity.ok(profileService.unfollowUser(username, loggedUser))
}