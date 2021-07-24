package dev.josers.realworld.service

import dev.josers.realworld.model.Following
import dev.josers.realworld.model.Profile
import dev.josers.realworld.model.User
import dev.josers.realworld.repository.FollowingRepository
import dev.josers.realworld.repository.UserRepository
import dev.josers.realworld.vo.response.ProfileResponseVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProfileService @Autowired constructor(
        val userRepository: UserRepository,
        val followingRepository: FollowingRepository) {

    fun followUser(username: String, loggedUser: User): ProfileResponseVO {
        val followed = userRepository.findByUsername(username)
        return if(followed != null) {

            followingRepository.save(Following(
                idFollowed = followed.id,
                idFollower = loggedUser.id
            ))

            ProfileResponseVO(profile = Profile(
                username = followed.username,
                bio = followed.bio,
                image = followed.image,
                following = true))
        } else ProfileResponseVO(profile = null)
    }

    fun unfollowUser(username: String, loggedUser: User): ProfileResponseVO {
        val followed = userRepository.findByUsername(username)
        if(followed != null) {
            followingRepository.deleteByIdFollowerAndIdFollowed(loggedUser.id, followed.id)
        }
        return ProfileResponseVO(profile = null)
    }

    fun getProfile(username: String): ProfileResponseVO {
        val user = userRepository.findByUsername(username)
        return if (user != null) {
            ProfileResponseVO(
                profile = Profile(
                    username = user.username,
                    bio = user.bio,
                    image = user.image,
                    following = false))
        } else ProfileResponseVO(profile = null)
    }
}