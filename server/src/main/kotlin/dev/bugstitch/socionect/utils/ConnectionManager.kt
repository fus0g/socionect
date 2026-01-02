package dev.bugstitch.socionect.utils

import io.ktor.server.websocket.DefaultWebSocketServerSession

object ConnectionManager {
    val userConnections = mutableMapOf<String, MutableSet<DefaultWebSocketServerSession>>()

    fun addConnection(userId: String, session: DefaultWebSocketServerSession) {
        val set = userConnections.getOrPut(userId) { mutableSetOf() }
        set.add(session)
    }

    fun removeConnection(userId: String, session: DefaultWebSocketServerSession) {
        userConnections[userId]?.remove(session)

        if (userConnections[userId]?.isEmpty() == true) {
            userConnections.remove(userId)
        }
    }

    fun getConnections(userId: String): Set<DefaultWebSocketServerSession> {
        return userConnections[userId] ?: emptySet()
    }

}

