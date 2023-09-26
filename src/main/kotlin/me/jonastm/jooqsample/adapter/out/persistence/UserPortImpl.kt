package me.jonastm.jooqsample.adapter.out.persistence

import me.jonastm.jooqsample.core.usercase.usermanagement.model.User
import me.jonastm.jooqsample.core.usercase.usermanagement.port.UserPort

class UserPortImpl : UserPort{

    override fun findUserByEmail(email: String?): User? {
        TODO("Not yet implemented")
    }

    override fun findUserById(id: String?): User? {
        TODO("Not yet implemented")
    }

    override fun createUser(user: User): User {
        TODO("Not yet implemented")
    }
}