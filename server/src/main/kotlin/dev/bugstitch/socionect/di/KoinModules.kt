package dev.bugstitch.socionect.di

import dev.bugstitch.socionect.data.database.repository.OneToOneChatDaoImpl
import dev.bugstitch.socionect.data.database.repository.UserDaoImpl
import dev.bugstitch.socionect.data.repository.OneToOneChatRepositoryImpl
import dev.bugstitch.socionect.data.repository.UserRepositoryImpl
import dev.bugstitch.socionect.domain.database.repository.OneToOneChatDao
import dev.bugstitch.socionect.domain.database.repository.UserDao
import dev.bugstitch.socionect.domain.repository.OneToOneChatRepository
import dev.bugstitch.socionect.domain.repository.UserRepository
import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.dsl.module

val KoinModule = module {

    single<Database>{
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/socionect",
            driver = "org.postgresql.Driver",
            user = "user1",
            password = "password"
        )
    }

    single<UserDao>{ UserDaoImpl(get()) }
    single<OneToOneChatDao> { OneToOneChatDaoImpl(get<Database>()) }

    single<UserRepository>{ UserRepositoryImpl(get()) }
    single<OneToOneChatRepository> { OneToOneChatRepositoryImpl(get(),get()) }

}