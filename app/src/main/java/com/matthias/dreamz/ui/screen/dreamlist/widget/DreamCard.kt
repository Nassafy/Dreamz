package com.matthias.dreamz.ui.screen.dreamlist.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matthias.dreamz.ui.screen.dreamlist.DreamDayState

@Composable
fun DreamCard(dreamDay: DreamDayState, modifier: Modifier = Modifier) {
//    val border = if (dreamDay.lucid) BorderStroke(
//        width = 1.dp,
//        color = MaterialTheme.colors.primary
//    ) else null
    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        elevation = if (dreamDay.lucid) 8.dp else 4.dp
//        border = border
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Column {
                Text(dreamDay.date, style = TextStyle(fontSize = 14.sp))
                Column {
                    dreamDay.dreams.forEach {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(it.name)
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                if (it.lucid) {
                                    Text(
                                        text = "Lucid",
                                        style = TextStyle(
                                            color = MaterialTheme.colors.primary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.width(15.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colors.background,
                                    modifier = Modifier.size(28.dp),
                                    elevation = 15.dp
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("${it.note}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}