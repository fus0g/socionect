package dev.bugstitch.socionect.data.repository

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

actual class PreferenceStore {

    lateinit var factory: Settings.Factory
    lateinit var settings: Settings
    fun init(context: Context)
    {
        factory = SharedPreferencesSettings.Factory(context)
        settings = factory.create("android_preferences")
    }

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
