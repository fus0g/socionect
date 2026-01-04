package dev.bugstitch.socionect.routes

import dev.bugstitch.socionect.data.models.OrganisationSubtopicDTO
import dev.bugstitch.socionect.data.models.OrganisationSubtopicMemberDTO
import dev.bugstitch.socionect.data.models.toOrganisationSubtopic
import dev.bugstitch.socionect.data.models.toOrganisationSubtopicMember
import dev.bugstitch.socionect.domain.models.toOrganisationSubtopicDTO
import dev.bugstitch.socionect.domain.models.toOrganisationSubtopicMemberDTO
import dev.bugstitch.socionect.domain.repository.OrganisationSubtopicRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.websocket.webSocket

fun Application.organisationSubtopicRouting(organisationSubtopicRepository: OrganisationSubtopicRepository){
    routing {
        authenticate("auth-jwt-user"){

            post("/organisation/subtopic/create")
            {
                val organisationSubtopic = call.receive<OrganisationSubtopicDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationSubtopicRepository.createSubtopic(organisationSubtopic.toOrganisationSubtopic(),userId)
                if (result != null) {
                    call.respond(result.toOrganisationSubtopicDTO())
                }
                else {
                    call.respond(HttpStatusCode.NotFound, "Failed to create organisation subtopic")
                }
            }

            post("/organisation/subtopic/get")
            {
                val organisationId = call.receive<OrganisationSubtopicDTO>()
                val result = organisationSubtopicRepository.getAllSubtopics(organisationId.organisationId)
                val lst = result.map { it.toOrganisationSubtopicDTO() }

                call.respond(lst)
            }

            post("/organisation/subtopic/delete"){
                val organisationSubtopic = call.receive<OrganisationSubtopicDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationSubtopicRepository.deleteSubtopic(organisationSubtopic.id,userId)
                if(result){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            post("/organisation/subtopic/addMember"){
                val organisationSubtopicMember = call.receive<OrganisationSubtopicMemberDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationSubtopicRepository.addSubTopicMember(organisationSubtopicMember.toOrganisationSubtopicMember(),userId)
                if(result){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.InternalServerError)
                }

            }

            post("/organisation/subtopic/getMembers"){
                val organisationSubtopicId = call.receive<OrganisationSubtopicDTO>()
                val result = organisationSubtopicRepository.getSubtopicMembers(organisationSubtopicId.id)
                call.respond(result.map { it.toOrganisationSubtopicMemberDTO() })
            }

            post("/organisation/subtopic/getMember"){
                val organisationSubtopicId = call.receive<OrganisationSubtopicDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationSubtopicRepository.getSubtopicMember(organisationSubtopicId.id,userId)
                if(result != null){
                    call.respond(result.toOrganisationSubtopicMemberDTO())
                }else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            post("/organisation/subtopic/removeMember"){
                val organisationSubtopicId = call.receive<OrganisationSubtopicDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationSubtopicRepository.removeSubTopicMember(organisationSubtopicId.id,userId)
                if(result){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            post("/organisation/subtopic/updateMember"){
                val organisationSubtopicMember = call.receive<OrganisationSubtopicMemberDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationSubtopicRepository.updateSubTopicMember(organisationSubtopicMember.toOrganisationSubtopicMember(),userId)
                if(result){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            webSocket("/organisation/subtopic/chat/{subtopicId}"){
                val principal = call.principal<JWTPrincipal>() ?: return@webSocket
                val senderId = principal.payload.getClaim("id").asString()
                val subtopicId = call.parameters["subtopicId"] ?: return@webSocket
            }

        }
    }
}