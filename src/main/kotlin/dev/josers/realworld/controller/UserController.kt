package dev.josers.realworld.controller

import dev.josers.realworld.model.User
import dev.josers.realworld.request.UserLoginRequestVO
import dev.josers.realworld.request.UserRequestVO
import dev.josers.realworld.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class UserController(@Autowired val userService: UserService){

    @PostMapping("/api/users")
    fun register(@RequestBody request: UserRequestVO) =
            ResponseEntity.ok(userService.registerUser(request))

    @PostMapping("/api/users/login")
    fun login(@RequestBody request: UserLoginRequestVO) =
            ResponseEntity.ok(userService.loginUser(request))

    @GetMapping("/api/user")
    fun retrieve(request: UserRequestVO, loggedUser: User) =
            ResponseEntity.ok(userService.getUser(request, loggedUser))

    @PutMapping("api/users")
    fun update(@RequestBody request: UserRequestVO, loggedUser: User) =
        ResponseEntity.ok(userService.updateUser(request, loggedUser))

}