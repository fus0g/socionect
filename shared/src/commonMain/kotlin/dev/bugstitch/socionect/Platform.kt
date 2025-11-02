package dev.bugstitch.socionect

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform