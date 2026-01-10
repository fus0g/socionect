package dev.bugstitch.socionect.data.repository

import dev.bugstitch.socionect.EMU_SERVER
import dev.bugstitch.socionect.SERVER
import dev.bugstitch.socionect.WEB_SERVER
import dev.bugstitch.socionect.data.models.TokenDTO
import dev.bugstitch.socionect.data.models.UserDTO
import dev.bugstitch.socionect.data.models.UserSearchRequest
import dev.bugstitch.socionect.data.models.toUser
import dev.bugstitch.socionect.domain.models.User
import dev.bugstitch.socionect.domain.models.toUserDTO
import dev.bugstitch.socionect.domain.repository.UserRepository
import dev.bugstitch.socionect.utils.CustomLog
import dev.bugstitch.socionect.utils.GlobalUser
import dev.bugstitch.socionect.utils.NetworkResult
import dev.bugstitch.socionect.utils.platform
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(private val httpClient: HttpClient): UserRepository {

    val endpoint = if(platform == "android") EMU_SERVER else if (platform == "web") WEB_SERVER else SERVER

    override suspend fun login(user: User): Flow<NetworkResult<TokenDTO>> {
        return flow {
            emit(NetworkResult.Loading())
            try {
                val response = httpClient.post("$endpoint/login"){
                    contentType(ContentType.Application.Json)
                    setBody(user.toUserDTO())
                }

                if(response.status == HttpStatusCode.OK)
                {
                    val tokens = response.body<TokenDTO>()
                    helloUser(tokens.token).collect {  }
                    emit(NetworkResult.Success(tokens))
                }
                else
                {
                    emit(NetworkResult.Error(response.status.description))
                }

            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun signUp(user: User): Flow<NetworkResult<TokenDTO>> {
        return flow {
            emit(NetworkResult.Loading())

            try {
                val response = httpClient.post("$endpoint/signup"){
                    contentType(ContentType.Application.Json)
                    setBody(user.toUserDTO())
                }
                if(response.status == HttpStatusCode.Created)
                {
                    val newRequest = httpClient.post("$endpoint/login"){
                        contentType(ContentType.Application.Json)
                        setBody(user.toUserDTO())
                    }
                    if(newRequest.status == HttpStatusCode.OK)
                    {
                        val tokens = newRequest.body<TokenDTO>()
                        helloUser(tokens.token).collect {  }
                        emit(NetworkResult.Success(tokens))
                    }
                    else
                    {
                        emit(NetworkResult.Error(newRequest.status.description))
                    }
                }
                else
                {
                    emit(NetworkResult.Error(response.status.description))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }

        }
    }

    override suspend fun checkUserNameConflict(user: User): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())

            try {
                val response = httpClient.post("$endpoint/user/username") {
                    contentType(ContentType.Application.Json)
                    setBody(user.toUserDTO())
                }
                if (response.status == HttpStatusCode.OK) {
                    // user exists → conflict
                    emit(NetworkResult.Success(true))
                } else if (response.status == HttpStatusCode.NotFound) {
                    // user not found → available
                    emit(NetworkResult.Success(false))
                } else {
                    emit(NetworkResult.Error("Unexpected response: ${response.status}"))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun checkEmailConflict(user: User): Flow<NetworkResult<Boolean>> {
        return flow {
            emit(NetworkResult.Loading())

            try {
                val response = httpClient.post("$endpoint/user/email") {
                    contentType(ContentType.Application.Json)
                    setBody(user.toUserDTO())
                }
                if (response.status == HttpStatusCode.OK) {
                    // user exists → conflict
                    emit(NetworkResult.Success(true))
                } else if (response.status == HttpStatusCode.NotFound) {
                    // user not found → available
                    emit(NetworkResult.Success(false))
                } else {
                    emit(NetworkResult.Error("Unexpected response: ${response.status}"))
                }
            }catch (e: Exception){
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }

    override suspend fun helloUser(
        jwt: String,
    ): Flow<NetworkResult<Boolean>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = httpClient.get("$endpoint/user/hello") {
                contentType(ContentType.Application.Json)
                headers {
                    append(HttpHeaders.Authorization, "Bearer $jwt")
                }
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val user = response.body<UserDTO>()
                    GlobalUser.User = user.toUser()
                    emit(NetworkResult.Success(true))
                }
                HttpStatusCode.Unauthorized -> emit(NetworkResult.Error("Unauthorized"))
                else -> emit(NetworkResult.Error("Unexpected error: ${response.status}"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Something went wrong"))
        }
    }


    override suspend fun refreshToken(refreshToken: String): Flow<NetworkResult<TokenDTO>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = httpClient.post("$endpoint/refresh") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("refresh_token" to refreshToken))
            }

            if (response.status == HttpStatusCode.OK) {
                val tokenDto = response.body<TokenDTO>()
                emit(NetworkResult.Success(tokenDto))
            } else {
                emit(NetworkResult.Error("Refresh token invalid or expired"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Something went wrong"))
        }
    }

    override suspend fun searchUsers(jwt: String, query: String): Flow<NetworkResult<List<User>>> {
        return flow {
            emit(NetworkResult.Loading())

            try {
                val response = httpClient.post("$endpoint/users/search") {
                    contentType(ContentType.Application.Json)
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $jwt")
                    }
                    setBody(UserSearchRequest(query))
                }

                when (response.status) {
                    HttpStatusCode.OK -> {
                        val users = response.body<List<UserDTO>>().map { it.toUser() }
                        emit(NetworkResult.Success(users))
                    }

                    HttpStatusCode.BadRequest -> {
                        emit(NetworkResult.Error("Invalid query"))
                    }

                    else -> {
                        emit(NetworkResult.Error("Unexpected: ${response.status}"))
                    }
                }

            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }
    }


}