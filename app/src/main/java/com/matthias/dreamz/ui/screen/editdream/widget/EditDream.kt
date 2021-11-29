package com.matthias.dreamz.ui.screen.editdream.widget

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.RelocationRequester
import androidx.compose.ui.layout.relocationRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.LocalWindowInsets
import com.matthias.dreamz.ui.screen.editdream.DreamState
import kotlinx.coroutines.delay

@ExperimentalComposeUiApi
@Composable
fun EditDream(
    dream: DreamState,
    save: (dream: DreamState) -> Unit,
    delete: (dreamId: Long) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused = interactionSource.collectIsFocusedAsState()
    val relocationRequester = remember { RelocationRequester() }
    val ime = LocalWindowInsets.current.ime
    LaunchedEffect(focused.value) {
        if (ime.isVisible && focused.value) {
            delay(300L)
            relocationRequester.bringIntoView()
        }
    }

    val title = rememberSaveable(dream) {
        mutableStateOf(dream.name)
    }
    val content = rememberSaveable(dream) {
        mutableStateOf(dream.text)
    }
    val textNote = rememberSaveable(dream) {
        mutableStateOf(dream.textNote)
    }
    val showNote = rememberSaveable {
        mutableStateOf(textNote.value?.isNotEmpty() ?: false)
    }

    Surface(
        modifier = Modifier
            .relocationRequester(relocationRequester)
            .padding(8.dp)
            .border(
                3.dp,
                if (!focused.value) Color.DarkGray else MaterialTheme.colors.primary,
                shape = RoundedCornerShape(10.dp),
            )
            .clip(RoundedCornerShape(10.dp)),
        elevation = 1.dp

    ) {
        val linear = Brush.linearGradient(
            listOf(
                MaterialTheme.colors.primary,
                MaterialTheme.colors.secondary
            )
        )
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            BasicTextField(
                value = title.value,
                onValueChange = {
                    title.value = it
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                singleLine = true,
                cursorBrush = linear,
                interactionSource = interactionSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (!it.isFocused) {
                            save(dream.copy(name = title.value))
                        }
                    },

                )
            Spacer(modifier = Modifier.height(10.dp))
            BasicTextField(
                value = content.value,
                onValueChange = {
                    content.value = it
                },
                textStyle = TextStyle(color = MaterialTheme.colors.onBackground, fontSize = 16.sp),
                cursorBrush = linear,
                interactionSource = interactionSource,
                modifier = Modifier
                    .heightIn(min = 100.dp, max = Dp.Infinity)
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (!it.isFocused) {
                            save(dream.copy(text = content.value))
                        }
                    },
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val color =
                    MaterialTheme.colors.primary.copy(alpha = if (dream.lucid) ContentAlpha.high else ContentAlpha.disabled)
                TextButton(
                    onClick = {
                        save(
                            dream.copy(
                                text = content.value,
                                name = title.value,
                                lucid = !dream.lucid
                            )
                        )
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = color
                    ),
                ) {
                    Text("Lucid")
                }
                IconButton(onClick = { showNote.value = !showNote.value }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit note")
                }

                if (title.value.isEmpty() && content.value.isEmpty() && textNote.value?.isEmpty() == true) {

                    IconButton(onClick = { delete(dream.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "delete")
                    }
                }
            }
            if (showNote.value) {
                Surface(
                    elevation = 3.dp, modifier = Modifier.clip(RoundedCornerShape(10.dp))
                ) {
                    BasicTextField(
                        value = textNote.value ?: "",
                        onValueChange = {
                            textNote.value = it
                        },
                        textStyle = TextStyle(
                            color = MaterialTheme.colors.onBackground.copy(alpha = 0.8f),
                            fontSize = 16.sp
                        ),
                        cursorBrush = linear,
                        interactionSource = interactionSource,
                        modifier = Modifier
                            .heightIn(min = 100.dp, max = Dp.Infinity)
                            .fillMaxWidth()
                            .padding(3.dp)
                            .onFocusChanged {
                                if (!it.isFocused) {
                                    save(dream.copy(textNote = textNote.value))
                                }
                            },
                    )
                }
            }
        }

    }
}