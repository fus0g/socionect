package dev.bugstitch.socionect.utils

fun getChatId(userA: String, userB: String): String {
    return listOf(userA, userB).sorted().joinToString("_")
}