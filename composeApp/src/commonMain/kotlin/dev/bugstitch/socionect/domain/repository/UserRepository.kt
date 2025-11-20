package dev.bugstitch.socionect.domain.repository

import dev.bugstitch.socionect.data.models.TokenDTO
import dev.bugstitch.socionect.domain.models.User
import dev.bugstitch.socionect.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun login(user: User): Flow<NetworkResult<TokenDTO>>

    suspend fun signUp(user: User): Flow<NetworkResult<TokenDTO>>

    suspend fun refreshToken(refreshToken: String): Flow<NetworkResult<TokenDTO>>

    suspend fun checkUserNameConflict(user:User):Flow<NetworkResult<Boolean>>

    suspend fun checkEmailConflict(user:User):Flow<NetworkResult<Boolean>>

    suspend fun helloUser(jwt: String): Flow<NetworkResult<Boolean>>

    suspend fun searchUsers(jwt:String,query: String): Flow<NetworkResult<List<User>>>
}