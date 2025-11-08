package dev.bugstitch.socionect.data.repository

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import java.util.prefs.Preferences

actual class PreferenceStore {
    private val settings: Settings = PreferencesSettings(Preferences.userRoot().node("socionect_prefs"))

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