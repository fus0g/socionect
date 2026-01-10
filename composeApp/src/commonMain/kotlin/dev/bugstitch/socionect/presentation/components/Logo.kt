package dev.bugstitch.socionect.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bugstitch.socionect.presentation.components.icons.SocionectLogo

@Composable
fun Logo(
    modifier: Modifier = Modifier,
    fontSize: Int
)
{
    Box(){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SocionectLogo(modifier)
            Spacer(modifier = Modifier.height(30.dp))
            Text("Socionect", fontSize = fontSize.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}