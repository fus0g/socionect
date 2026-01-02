package dev.bugstitch.socionect.routes

import dev.bugstitch.socionect.data.models.OrganisationDTO
import dev.bugstitch.socionect.data.models.OrganisationJoinReceivedRequestDTO
import dev.bugstitch.socionect.data.models.OrganisationJoinSentRequestDTO
import dev.bugstitch.socionect.data.models.OrganisationMemberDTO
import dev.bugstitch.socionect.data.models.toOrganisation
import dev.bugstitch.socionect.data.models.toOrganisationJoinReceivedRequest
import dev.bugstitch.socionect.data.models.toOrganisationJoinSentRequest
import dev.bugstitch.socionect.data.models.toOrganisationMember
import dev.bugstitch.socionect.domain.models.OrganisationMember
import dev.bugstitch.socionect.domain.models.toOrganisationDTO
import dev.bugstitch.socionect.domain.models.toOrganisationJoinReceivedRequestDTO
import dev.bugstitch.socionect.domain.models.toOrganisationJoinSentRequestDTO
import dev.bugstitch.socionect.domain.repository.OrganisationRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.organisationRouting(organisationRepository: OrganisationRepository){
    routing {
        authenticate("auth-jwt-user"){

            post("/organisation/create"){
                val organisation = call.receive<OrganisationDTO>()

                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()

                val result = organisationRepository.createOrganisation(organisation.toOrganisation(),userId)
                if (result != null) {
                    call.respond(result.toOrganisationDTO())
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Failed to create organisation")
                }
            }

            post("/organisation/search") {
                val name = call.receive<OrganisationDTO>()
                val result = organisationRepository.getOrganisationsByMatchingName(name.name)
                call.respond(result.map { it.toOrganisationDTO() })
            }

            post("/organisation/get") {
                val id = call.receive<OrganisationDTO>()
                val result = organisationRepository.getOrganisation(id.id)
                if (result != null) {
                    call.respond(result.toOrganisationDTO())
                } else {
                    call.respond(HttpStatusCode.NotFound, "Organisation not found")
                }
            }

            //send request to a user
            post("/organisation/sendRequestToUser"){
                val organisationJoinSentRequest = call.receive<OrganisationJoinSentRequestDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()

                val result = organisationRepository.sendOrganisationJoinSentRequest(organisationJoinSentRequest.toOrganisationJoinSentRequest(),userId)
                if(result){
                    call.respond(HttpStatusCode.Created)
                }else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            //send by org to user
            post("/organisation/getSentRequests"){
                val organisationId = call.receive<OrganisationDTO>()
                val result = organisationRepository.getAllOrganisationJoinSentRequests(organisationId.id)
                call.respond(result.map { it.toOrganisationJoinSentRequestDTO() })
            }


            //confirmed by user
            post("/organisation/confirmSentRequest"){
                val organisationJoinSentRequest = call.receive<OrganisationJoinSentRequestDTO>()

                val result = organisationRepository.confirmOrganisationJoinSentRequest(organisationJoinSentRequest.toOrganisationJoinSentRequest())
                if(result){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.InternalServerError)
                }

            }

            //declined by user
            post("/organisation/declineSentRequest"){
                val organisationJoinSentRequest = call.receive<OrganisationJoinSentRequestDTO>()
                val result = organisationRepository.declineOrganisationJoinSentRequest(organisationJoinSentRequest.toOrganisationJoinSentRequest())
                if(result){
                    call.respond(HttpStatusCode.OK)
                }
                else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            //requests received by org from user
            post("/organisation/getReceivedRequests"){
                val organisationId = call.receive<OrganisationDTO>()
                val result = organisationRepository.getAllOrganisationJoinReceivedRequests(organisationId.id)
                call.respond(result.map { it.toOrganisationJoinReceivedRequestDTO() })
            }

            // all requests send by user
            get("/organisation/getSentRequestsForUser"){
                val principal = call.principal<JWTPrincipal>() ?: return@get
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationRepository.getOrganisationJoinReceivedRequestsForUser(userId)
                call.respond(result.map { it.toOrganisationJoinReceivedRequestDTO() })
            }

            // all requests received by user
            get("/organisation/getReceivedRequestsForUser"){
                val principal = call.principal<JWTPrincipal>() ?: return@get
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationRepository.getOrganisationJoinSentRequestsForUser(userId)
                call.respond(result.map { it.toOrganisationJoinSentRequestDTO() })
            }

            //user sends request to org
            post("/organisation/confirmReceivedRequest"){
                val organisationJoinReceivedRequest = call.receive<OrganisationJoinReceivedRequestDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationRepository.sendOrganisationJoinReceivedRequest(organisationJoinReceivedRequest.toOrganisationJoinReceivedRequest().copy(userId = userId))
                if(result){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            //confirmed by org
            post("/organisation/declineReceivedRequest"){
                val organisationJoinReceivedRequest = call.receive<OrganisationJoinReceivedRequestDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationRepository.confirmOrganisationJoinReceivedRequest(organisationJoinReceivedRequest.toOrganisationJoinReceivedRequest(),userId)
                if(result){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            //declined by org
            post("/organisation/declineReceivedRequest"){
                val organisationJoinReceivedRequest = call.receive<OrganisationJoinReceivedRequestDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationRepository.declineOrganisationJoinReceivedRequest(organisationJoinReceivedRequest.toOrganisationJoinReceivedRequest(),userId)
                if(result){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

        }
    }
}