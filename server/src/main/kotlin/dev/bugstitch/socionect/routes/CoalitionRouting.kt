package dev.bugstitch.socionect.routes

import dev.bugstitch.socionect.data.models.CoalitionDTO
import dev.bugstitch.socionect.data.models.CoalitionRequestDTO
import dev.bugstitch.socionect.data.models.CreateCoalitionDTO
import dev.bugstitch.socionect.data.models.OrganisationDTO
import dev.bugstitch.socionect.data.models.toCoalition
import dev.bugstitch.socionect.data.models.toCoalitionRequest
import dev.bugstitch.socionect.data.models.toOrganisation
import dev.bugstitch.socionect.domain.models.coalitionRequestDTO
import dev.bugstitch.socionect.domain.models.toCoalitionDTO
import dev.bugstitch.socionect.domain.models.toOrganisationDTO
import dev.bugstitch.socionect.domain.repository.CoalitionRepository
import dev.bugstitch.socionect.utils.routes.CoalitionRoutes
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing


fun Application.coalitionRouting(
    coalitionRepository: CoalitionRepository
){
    routing {
        authenticate("auth-jwt-user") {

            post(CoalitionRoutes.CREATE){
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val colReq = call.receive<CreateCoalitionDTO>()

                val created = coalitionRepository.createCoalition(
                    colReq.coalitionDTO.toCoalition(),
                    colReq.hostOrg.toOrganisation(),
                    userId,
                    colReq.organisations.map { it.toOrganisation() }
                    )

                if(created){
                    call.respond(HttpStatusCode.Created)
                }
                else call.respond(HttpStatusCode.Conflict)

            }

            post(CoalitionRoutes.GET_ALL_COALITION){
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val org = call.receive<OrganisationDTO>()

                val cols = coalitionRepository.getAllOrganisationCoalition(
                    id = org.id,
                    userId = userId,
                )

                call.respond(cols.map { it.toCoalitionDTO() })
            }

            post(CoalitionRoutes.GET_ALL_ORGANISATIONS) {
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val coal = call.receive<CoalitionDTO>()
                val orgs = coalitionRepository.getAllOrganisationsInCoalition(coal.id)
                call.respond(orgs.map { it.toOrganisationDTO()})
            }

            post(CoalitionRoutes.GET_COALITION){
                val coal = call.receive<CoalitionDTO>()
                val nCoal = coalitionRepository.getCoalition(coal.id)
                if(nCoal != null){
                    call.respond(nCoal.toCoalitionDTO())
                }
                else call.respond(HttpStatusCode.NotFound)
            }

            post(CoalitionRoutes.CREATE_REQUEST){
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val request = call.receive<CoalitionRequestDTO>()
                val created = coalitionRepository.createCoalitionRequest(request.toCoalitionRequest(),userId)
                if(created){
                    call.respond(HttpStatusCode.Created)
                }else call.respond(HttpStatusCode.Conflict)
            }

            post(CoalitionRoutes.GET_COALITION_REQUEST){
                val org = call.receive<OrganisationDTO>()

                val requests = coalitionRepository.getAllCoalitionRequests(org.id)
                call.respond(requests.map { it.coalitionRequestDTO() })
            }

            post(CoalitionRoutes.ACCEPT_REQUEST)
            {
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val request = call.receive<CoalitionRequestDTO>()
                val acceptRequest = coalitionRepository.acceptCoalitionRequest(request.toCoalitionRequest(),userId)
                if(acceptRequest){
                    call.respond(HttpStatusCode.Accepted)
                }else call.respond(HttpStatusCode.NotFound)
            }

            post(CoalitionRoutes.DECLINE_REQUEST)
            {
                val principal = call.principal<JWTPrincipal>() ?: return@post
                val userId = principal.payload.getClaim("id").asString()
                val request = call.receive<CoalitionRequestDTO>()
                val declineRequest  = coalitionRepository.declineCoalitionRequest(request.toCoalitionRequest(),userId)
                if(declineRequest){
                    call.respond(HttpStatusCode.Accepted)
                }else call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}