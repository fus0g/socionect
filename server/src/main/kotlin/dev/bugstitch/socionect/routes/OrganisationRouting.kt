package dev.bugstitch.socionect.routes

import dev.bugstitch.socionect.data.models.OrganisationDTO
import dev.bugstitch.socionect.data.models.RequestSendByUserDTO
import dev.bugstitch.socionect.data.models.RequestSendByOrganisationDTO
import dev.bugstitch.socionect.data.models.toOrganisation
import dev.bugstitch.socionect.data.models.toRequestSendByUser
import dev.bugstitch.socionect.data.models.toRequestSendByOrganisation
import dev.bugstitch.socionect.domain.models.toOrganisationDTO
import dev.bugstitch.socionect.domain.models.toRequestSendByUserDTO
import dev.bugstitch.socionect.domain.models.toRequestSendByOrganisationDTO
import dev.bugstitch.socionect.domain.models.toUserDTO
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

            get("/organisation/userOrganisations"){
                val principal = call.principal<JWTPrincipal>() ?: return@get
                val userId = principal.payload.getClaim("id").asString()

                val result = organisationRepository.getUserOrganisations(userId)

                call.respond(result.map { it.toOrganisationDTO() })

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
                val organisationJoinSentRequest = call.receive<RequestSendByOrganisationDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()

                val result = organisationRepository.organisationSendRequest(organisationJoinSentRequest.toRequestSendByOrganisation(),userId)
                if(result){
                    call.respond(HttpStatusCode.Created)
                }else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            //send by org to user
            post("/organisation/getRequestsSendToUser"){
                val organisationId = call.receive<OrganisationDTO>()
                val result = organisationRepository.getAllRequestsSendByOrganisation(organisationId.id)
                call.respond(result.map { it.toUserDTO() })
            }


            //confirmed by user
            post("/organisation/userConfirmRequestReceivedFromOrganisation"){
                val organisationJoinSentRequest = call.receive<RequestSendByOrganisationDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationRepository.requestConfirmedByUser(
                    organisationJoinSentRequest.toRequestSendByOrganisation()
                        .copy(userId = userId)
                )
                if(result){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.InternalServerError)
                }

            }

            //declined by user
            post("/organisation/userDeclineRequestFromOrganisation"){
                val organisationJoinSentRequest = call.receive<RequestSendByOrganisationDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationRepository.requestDeclinedByUser(
                    organisationJoinSentRequest.toRequestSendByOrganisation()
                    .copy(userId = userId)
                )
                if(result){
                    call.respond(HttpStatusCode.OK)
                }
                else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            //requests received by org from user
            post("/organisation/getRequestsReceivedByOrganisation"){
                val organisationId = call.receive<OrganisationDTO>()
                val result = organisationRepository.getAllRequestsReceivedByOrganisation(organisationId.id)
                call.respond(result.map { it.toUserDTO() })
            }

            // all requests send by user
            get("/organisation/getRequestsSendByUser"){
                val principal = call.principal<JWTPrincipal>() ?: return@get
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationRepository.getAllRequestSendByUser(userId)
                call.respond(result.map { it.toOrganisationDTO() })
            }

            // all requests received by user
            get("/organisation/getRequestsReceivedByUser"){
                val principal = call.principal<JWTPrincipal>() ?: return@get
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationRepository.getAllRequestsReceivedByUser(userId)
                call.respond(result.map { it.toOrganisationDTO() })
            }

            //user sends request to org
            post("/organisation/userSendRequestToOrganisation"){
                val organisationJoinReceivedRequest = call.receive<RequestSendByUserDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationRepository.userSendRequest(organisationJoinReceivedRequest.toRequestSendByUser().copy(userId = userId))
                if(result){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            //confirmed by org
            post("/organisation/organisationConfirmsRequestByUser"){
                val organisationJoinReceivedRequest = call.receive<RequestSendByUserDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationRepository.requestConfirmedByOrganisation(organisationJoinReceivedRequest.toRequestSendByUser(),userId)
                if(result){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            //declined by org
            post("/organisation/requestReceivedFromUserDeclinedByOrganisation"){
                val organisationJoinReceivedRequest = call.receive<RequestSendByUserDTO>()
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val result = organisationRepository.requestDeclinedByOrganisation(organisationJoinReceivedRequest.toRequestSendByUser(),userId)
                if(result){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            post("/organisation/getAllMembers"){
                val org = call.receive<OrganisationDTO>()
                val result = organisationRepository.getOrganisationMembers(org.id)
                if(result.isNotEmpty()){
                    call.respond(result.map { it.toUserDTO() })
                }
            }

        }
    }
}