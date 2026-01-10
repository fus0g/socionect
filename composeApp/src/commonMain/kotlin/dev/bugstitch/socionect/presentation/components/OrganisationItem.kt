package dev.bugstitch.socionect.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import dev.bugstitch.socionect.domain.models.Organisation
import dev.bugstitch.socionect.presentation.theme.CustomColors

@Composable
fun OrganisationItem(organisation: Organisation,
                     onItemClick: () -> Unit) {

    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp)
    ) {
        Row(modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .clickable(
                onClick = onItemClick
            )
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(32.dp))
            .padding(12.dp)){
            Box(modifier = Modifier.size(50.dp)
                .background(
                    color = CustomColors.PastelColours.pastelColorFor(organisation.id),
                    shape = CircleShape
                ),
                contentAlignment = Alignment.Center
                ){

                Text("${
                    if(organisation.name.isNotEmpty())
                    {
                        organisation.name[0]
                    }else{
                        "*"
                    }
                }",
                    fontWeight = FontWeight.ExtraBold)

            }
            Box(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier){
                Text(organisation.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium)
                Text(organisation.description,
                    fontSize = 12.sp,
                    color = Color.Gray)
            }
        }
    }

}