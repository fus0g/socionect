package dev.bugstitch.socionect.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.bugstitch.socionect.presentation.components.icons.LucideBadgePlus
import dev.bugstitch.socionect.presentation.components.icons.LucideMessageSquareText
import dev.bugstitch.socionect.presentation.components.icons.LucideSearch
import dev.bugstitch.socionect.presentation.components.icons.LucideUser

@Composable
fun NavigationBar(
    onChatClick: () -> Unit,
    onSearchClick:() -> Unit,
    onCreateClick:() -> Unit,
    onProfileClick:() -> Unit,
    isLarge: Boolean,
    page: Int
){

    if(isLarge){

        Column(modifier = Modifier.fillMaxHeight()
            .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            IconButton(
                onClick = onChatClick,
                modifier = Modifier.padding(2.dp)
                    .border(2.dp,
                        if(page == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
            ){
                Icon(LucideMessageSquareText,"",
                    tint = if(page == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.padding(2.dp)
                    .border(2.dp,
                        if(page == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
            ){
                Icon(LucideSearch,"",
                    tint = if(page == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                onClick = onCreateClick,
                modifier = Modifier.padding(2.dp)
                    .border(2.dp,
                        if(page == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
            ){
                Icon(LucideBadgePlus,"",
                    tint = if(page == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                onClick = onProfileClick,
                modifier = Modifier.padding(2.dp)
                    .border(2.dp,
                        if(page == 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
            ){
                Icon(
                    LucideUser,"",
                    tint = if(page == 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
            }

        }

    }else{
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically){
            IconButton(
                onClick = onChatClick,
                modifier = Modifier.padding(2.dp)
                    .border(2.dp,
                        if(page == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
            ){
                Icon(LucideMessageSquareText,"",
                    tint = if(page == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.padding(2.dp)
                    .border(2.dp,
                        if(page == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
            ){
                Icon(LucideSearch,"",
                    tint = if(page == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                onClick = onCreateClick,
                modifier = Modifier.padding(2.dp)
                    .border(2.dp,
                        if(page == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
            ){
                Icon(LucideBadgePlus,"",
                    tint = if(page == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                onClick = onProfileClick,
                modifier = Modifier.padding(2.dp)
                    .border(2.dp,
                        if(page == 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                        shape = CircleShape
                    )
            ){
                Icon(
                    LucideUser,"",
                    tint = if(page == 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }

}