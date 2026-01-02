package dev.bugstitch.socionect.data.models

import dev.bugstitch.socionect.domain.models.Coalition
import kotlinx.serialization.Serializable

@Serializable
data class CoalitionDTO(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val createdAt: Long = 0
)

fun CoalitionDTO.toCoalition(): Coalition = Coalition(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt
)

