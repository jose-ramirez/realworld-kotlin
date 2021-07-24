package dev.josers.realworld.controller

import dev.josers.realworld.model.User
import dev.josers.realworld.service.UserService
import dev.josers.realworld.vo.request.UserLoginRequestVO
import dev.josers.realworld.vo.request.UserRequestVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import dev.josers.realworld.RestMethods.*
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class UserController(@Autowired val userService: UserService){

    @PostMapping(Users.V1.LOGIN)
    fun login(@RequestBody request: UserLoginRequestVO) =
            ResponseEntity.ok(userService.loginUser(request))

    @GetMapping(Users.V1.USER)
    fun retrieve(request: UserRequestVO, loggedUser: User) =
        ResponseEntity.ok(userService.getUser(request, loggedUser))

    @PostMapping(Users.V1.PATH)
    fun register(@RequestBody request: UserRequestVO) =
        ResponseEntity.ok(userService.registerUser(request))

    @PutMapping(Users.V1.PATH)
    fun update(@RequestBody request: UserRequestVO, loggedUser: User) =
        ResponseEntity.ok(userService.updateUser(request, loggedUser))
}