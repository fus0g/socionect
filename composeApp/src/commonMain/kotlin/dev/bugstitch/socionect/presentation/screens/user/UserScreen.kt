package dev.bugstitch.socionect.presentation.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bugstitch.socionect.presentation.theme.CustomColors
import dev.bugstitch.socionect.utils.GlobalUser

@Composable
fun UserScreen(
    onLogout: () -> Unit,
    navigateToUserRequests:()->Unit,
    isLarge: Boolean
){

    val baseModifier = if (isLarge) {
        Modifier
            .widthIn(max = 360.dp, min = 360.dp)
            .fillMaxHeight()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(top = 16.dp, start=16.dp,end=16.dp,bottom = 16.dp)
    } else {
        Modifier
            .fillMaxSize()
            .padding(top = 2.dp, start=8.dp,end=8.dp,bottom = 8.dp)
    }

    Column(
        modifier = baseModifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(GlobalUser.User != null)
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(100.dp)
                    .background(
                        color = CustomColors.PastelColours.pastelColorFor(GlobalUser.User!!.name),
                        shape = CircleShape
                    ),
                    contentAlignment = Alignment.Center
                ){
                    Text("${
                        if(GlobalUser.User!!.name.isNotEmpty())
                        {
                            GlobalUser.User!!.name[0]
                        }else{
                            "*"
                        }
                    }",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 48.sp)

                }
                Box(modifier = Modifier.height(8.dp))
                Text(GlobalUser.User!!.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp)
                Text(GlobalUser.User!!.email)
            }

        }

        Button(
            onClick = {
                onLogout()
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Logout")
        }
        Button(
            onClick = {navigateToUserRequests()},
        ){
            Text("Requests")
        }

    }

}