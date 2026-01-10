package dev.bugstitch.socionect.presentation.components.icons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap

@Composable
fun SocionectLogo(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .aspectRatio(1f)
    ) {
        val w = size.width
        val h = size.height

        fun x(v: Float) = v / 256f * w
        fun y(v: Float) = v / 256f * h
        fun r(v: Float) = v / 256f * minOf(w, h)

        val connectionColor = Color(0xFF6C7CFF)

        // --- Connections ---
        drawLine(
            color = connectionColor,
            start = Offset(x(128f), y(128f)),
            end = Offset(x(64f), y(80f)),
            strokeWidth = r(12f),
            cap = StrokeCap.Round
        )

        drawLine(
            color = connectionColor,
            start = Offset(x(128f), y(128f)),
            end = Offset(x(192f), y(80f)),
            strokeWidth = r(12f),
            cap = StrokeCap.Round
        )

        drawLine(
            color = connectionColor,
            start = Offset(x(128f), y(128f)),
            end = Offset(x(208f), y(128f)),
            strokeWidth = r(12f),
            cap = StrokeCap.Round
        )

        drawLine(
            color = connectionColor,
            start = Offset(x(128f), y(128f)),
            end = Offset(x(168f), y(196f)),
            strokeWidth = r(12f),
            cap = StrokeCap.Round
        )

        drawLine(
            color = connectionColor,
            start = Offset(x(128f), y(128f)),
            end = Offset(x(88f), y(196f)),
            strokeWidth = r(12f),
            cap = StrokeCap.Round
        )

        // --- Outer nodes ---
        drawCircle(
            color = Color(0xFF4ECDC4),
            radius = r(24f),
            center = Offset(x(64f), y(80f))
        )

        drawCircle(
            color = Color(0xFFFFD166),
            radius = r(24f),
            center = Offset(x(192f), y(80f))
        )

        drawCircle(
            color = Color(0xFFA78BFA),
            radius = r(24f),
            center = Offset(x(208f), y(128f))
        )

        drawCircle(
            color = Color(0xFFFF6B6B),
            radius = r(24f),
            center = Offset(x(168f), y(196f))
        )

        drawCircle(
            color = Color(0xFF2EC4B6),
            radius = r(24f),
            center = Offset(x(88f), y(196f))
        )

        // --- Central hub ---
        drawCircle(
            color = Color(0xFF5865F2),
            radius = r(34f),
            center = Offset(x(128f), y(128f))
        )
    }
}
