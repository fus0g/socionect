package dev.bugstitch.socionect.modules

import dev.bugstitch.socionect.data.database.tables.CoalitionMessages
import dev.bugstitch.socionect.data.database.tables.CoalitionOrganisationMembers
import dev.bugstitch.socionect.data.database.tables.CoalitionOrganisations
import dev.bugstitch.socionect.data.database.tables.CoalitionRequests
import dev.bugstitch.socionect.data.database.tables.Coalitions
import dev.bugstitch.socionect.data.database.tables.OneToOneChats
import dev.bugstitch.socionect.data.database.tables.OneToOneMessages
import dev.bugstitch.socionect.data.database.tables.OrganisationMembers
import dev.bugstitch.socionect.data.database.tables.OrganisationSubtopicMembers
import dev.bugstitch.socionect.data.database.tables.OrganisationSubtopicMessages
import dev.bugstitch.socionect.data.database.tables.OrganisationSubtopics
import dev.bugstitch.socionect.data.database.tables.Organisations
import dev.bugstitch.socionect.data.database.tables.OrganisationJoinReceivedRequests
import dev.bugstitch.socionect.data.database.tables.OrganisationJoinSentRequests
import dev.bugstitch.socionect.data.database.tables.Users
import io.ktor.server.application.Application
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Application.configureDatabase(database: Database){

    transaction(database) {
        SchemaUtils.create(Users)
        SchemaUtils.create(OneToOneChats)
        SchemaUtils.create(OneToOneMessages)

        SchemaUtils.create(Organisations)
        SchemaUtils.create(OrganisationMembers)
        SchemaUtils.create(OrganisationSubtopics)
        SchemaUtils.create(OrganisationSubtopicMembers)
        SchemaUtils.create(OrganisationSubtopicMessages)

        SchemaUtils.create(Coalitions)
        SchemaUtils.create(CoalitionOrganisations)
        SchemaUtils.create(CoalitionOrganisationMembers)
        SchemaUtils.create(CoalitionMessages)

        SchemaUtils.create(OrganisationJoinReceivedRequests)
        SchemaUtils.create(OrganisationJoinSentRequests)
        SchemaUtils.create(CoalitionRequests)

    }

}