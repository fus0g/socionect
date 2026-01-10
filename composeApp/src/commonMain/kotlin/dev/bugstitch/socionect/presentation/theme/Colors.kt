package dev.bugstitch.socionect.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import kotlin.math.abs

object CustomColors {

    // Core Accent – Fresh, trustworthy teal-cyan (pops nicely in dark mode)
    val LagoonTeal = Color(0xFF00D4C8)       // Main primary – sent messages, buttons, links
    val TealGlow   = Color(0xFF4DF0E6)       // Brighter hover/active variant

    // Deep, calming dark neutrals
    val MidnightBlack  = Color(0xFF0A0E17)    // True background
    val DeepSlate      = Color(0xFF141A28)    // Surface / cards / received message bubbles
    val SlateBorder    = Color(0xFF1E2538)    // Subtle dividers & outlines

    // Light & text colors
    val CloudWhite     = Color(0xFFF8FAFF)    // Main text in dark mode
    val MistGrey       = Color(0xFF9CA3BF)    // Secondary text, timestamps, subtle hints

    // Light mode version (clean & airy – less common but good fallback)
    val LightAqua      = Color(0xFFE0F7FA)
    val SoftTeal       = Color(0xFF26A69A)

    val LightColorScheme = lightColorScheme(
        primary = SoftTeal,
        onPrimary = Color.White,
        primaryContainer = LightAqua,
        onPrimaryContainer = Color(0xFF004D40),

        secondary = Color(0xFF455A64),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFCFD8DC),
        onSecondaryContainer = Color(0xFF263238),

        background = Color.White,
        onBackground = Color(0xFF0A0E17),

        surface = Color.White,
        onSurface = Color(0xFF0A0E17),
        surfaceVariant = Color(0xFFECEFF1),
        onSurfaceVariant = Color(0xFF455A64),

        outline = Color(0xFFB0BEC5),
        outlineVariant = Color(0xFFCFD8DC),

        error = Color(0xFFE53935),
        onError = Color.White
    )

    val DarkColorScheme = darkColorScheme(
        primary = LagoonTeal,
        onPrimary = MidnightBlack,           // Dark text on accent → excellent contrast
        primaryContainer = Color(0xFF00695C),
        onPrimaryContainer = TealGlow,

        secondary = MistGrey,
        onSecondary = CloudWhite,
        secondaryContainer = DeepSlate,
        onSecondaryContainer = CloudWhite,

        background = MidnightBlack,
        onBackground = CloudWhite,

        surface = DeepSlate,
        onSurface = CloudWhite,
        surfaceVariant = SlateBorder,
        onSurfaceVariant = MistGrey,

        outline = SlateBorder,
        outlineVariant = Color(0xFF263238),

        error = Color(0xFFEF5350),
        onError = MidnightBlack
    )

    object PastelColours {
        val PastelBlue = Color(0xFFAEC6CF)      // Soft pastel blue
        val PastelYellow = Color(0xFFFFE5A5)    // Soft pastel yellow
        val PastelPink = Color(0xFFFFD1DC)      // Soft pastel pink
        val PastelGreen = Color(0xFFB5EAD7)     // Light pastel mint green
        val PastelLavender = Color(0xFFE6E6FA)  // Light pastel lavender
        val PastelPeach = Color(0xFFFFDAC1)     // Light pastel peach
        val PastelMint = Color(0xFFB0EACD)      // Soft pastel mint
        val PastelLilac = Color(0xFFDBB8E3)     // Light pastel lilac
        val PastelOrange = Color(0xFFFFCCB6)    // Soft pastel orange
        val PastelAqua = Color(0xFFA2D8D8)      // Soft pastel aqua

        val colorList = listOf(
            PastelBlue,
            PastelPink,
            PastelGreen,
            PastelYellow,
            PastelLavender,
            PastelPeach,
            PastelMint,
            PastelLilac,
            PastelOrange,
            PastelAqua
        )

        fun pastelColorFor(input: String): Color {
            if (input.isEmpty()) return colorList.first()

            // Stable hash → index
            val hash = input.hashCode()
            val index = abs(hash) % colorList.size

            return colorList[index]
        }
    }
}