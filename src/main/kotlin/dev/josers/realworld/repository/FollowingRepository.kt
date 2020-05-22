package dev.josers.realworld.repository

import dev.josers.realworld.model.Following
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FollowingRepository: MongoRepository<Following, String> {
    fun deleteByIdFollowerAndIdFollowed(idFollower: String?, idFollowed: String?)
}