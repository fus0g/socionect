package dev.bugstitch.socionect.di

import dev.bugstitch.socionect.data.database.repository.OneToOneChatDaoImpl
import dev.bugstitch.socionect.data.database.repository.OrganisationDaoImpl
import dev.bugstitch.socionect.data.database.repository.OrganisationSubtopicDaoImpl
import dev.bugstitch.socionect.data.database.repository.UserDaoImpl
import dev.bugstitch.socionect.data.repository.OneToOneChatRepositoryImpl
import dev.bugstitch.socionect.data.repository.OrganisationRepositoryImpl
import dev.bugstitch.socionect.data.repository.OrganisationSubtopicRepositoryImpl
import dev.bugstitch.socionect.data.repository.UserRepositoryImpl
import dev.bugstitch.socionect.domain.database.repository.OneToOneChatDao
import dev.bugstitch.socionect.domain.database.repository.OrganisationDao
import dev.bugstitch.socionect.domain.database.repository.OrganisationSubtopicDao
import dev.bugstitch.socionect.domain.database.repository.UserDao
import dev.bugstitch.socionect.domain.repository.OneToOneChatRepository
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import dev.bugstitch.socionect.domain.repository.OrganisationSubtopicRepository
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
    single<OrganisationDao>{ OrganisationDaoImpl(get<Database>()) }
    single<OrganisationSubtopicDao>{ OrganisationSubtopicDaoImpl(get<Database>()) }


    single<UserRepository>{ UserRepositoryImpl(get()) }
    single<OneToOneChatRepository> { OneToOneChatRepositoryImpl(get(),get()) }
    single<OrganisationRepository> { OrganisationRepositoryImpl(get()) }
    single<OrganisationSubtopicRepository> { OrganisationSubtopicRepositoryImpl(get(), get()) }

}