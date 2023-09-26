package me.jonastm.jooqsample.core.usercase.usermanagement.port

import me.jonastm.jooqsample.core.usercase.usermanagement.model.User

interface UserPort {

    fun findUserByEmail(email: String?): User?
    fun findUserById(id: String?): User?
    fun createUser(user: User): User

}