package dev.bugstitch.socionect.data.repository

expect class PreferenceStore {
    fun setPreference(key: String, value: String): String
    fun getPreference(key: String): String?
    fun removePreference(key: String): String
}