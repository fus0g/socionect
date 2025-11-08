package dev.bugstitch.socionect.data.repository

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import kotlinx.browser.localStorage

actual class PreferenceStore {
    private val settings: Settings = StorageSettings(localStorage)

    actual fun setPreference(key: String, value: String): String {
        return try {
            settings.putString(key, value)
            "Success"
        } catch (e: Exception) {
            e.message ?: "Error"
        }
    }

    actual fun getPreference(key: String): String? {
        return settings.getStringOrNull(key)
    }

    actual fun removePreference(key: String): String {
        return try {
            settings.remove(key)
            "Success"
        } catch (e: Exception) {
            e.message ?: "Error"
        }
    }
}