package dev.bugstitch.socionect.domain.models

import dev.bugstitch.socionect.data.models.CoalitionDTO

data class Coalition(
    val id: String,
    val name: String,
    val description: String,
    val createdAt: Long
)

fun Coalition.toCoalitionDTO(): CoalitionDTO = CoalitionDTO(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt
)