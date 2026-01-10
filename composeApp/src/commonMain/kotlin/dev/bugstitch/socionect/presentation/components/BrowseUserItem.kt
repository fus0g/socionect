package dev.bugstitch.socionect.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bugstitch.socionect.domain.models.User
import dev.bugstitch.socionect.presentation.theme.CustomColors

@Composable
fun BrowseUserItem(
    user: User,
    isJoined: Boolean,
    isRequested: Boolean,
    onSendRequest: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(32.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = CustomColors.PastelColours
                            .pastelColorFor(user.id),
                        shape = RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.firstOrNull()?.toString() ?: "*",
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(Modifier.width(10.dp))

            Column {
                Text(
                    user.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    "@${user.username}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        when {
            isJoined -> {
                Text(
                    "Joined",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            isRequested -> {
                Text(
                    "Requested",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            else -> {
                Button(
                    onClick = onSendRequest,
                    shape = RoundedCornerShape(24.dp),
                    contentPadding = PaddingValues(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    )
                ) {
                    Text("Request", fontSize = 12.sp)
                }
            }
        }
    }
}
