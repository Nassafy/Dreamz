package com.matthias.dreamz.ui.screen.viewdream.widget

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.People
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.matthias.dreamz.R
import com.matthias.dreamz.data.model.DreamMetadata
import com.matthias.dreamz.ui.screen.viewdream.DreamState
import com.matthias.dreamz.ui.widget.AutocompleteTextField


@Composable
fun DreamView(
    dream: DreamState,
    saveMetadata: (metadata: DreamMetadata) -> Unit,
    peoplesSuggestions: List<String>,
    tagsSuggestions: List<String>,
    searchText: String?
) {
    Surface(
        elevation = 1.dp,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                3.dp, Color.DarkGray, shape = RoundedCornerShape(10.dp)
            )

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(dream.name, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                annotatedDream(dream.text, searchText),
                style = TextStyle(textAlign = TextAlign.Justify, fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (dream.textNote?.isNotEmpty() == true) {
                Surface(elevation = 6.dp, modifier = Modifier.clip(RoundedCornerShape(10.dp))) {
                    Text(
                        text = dream.textNote,
                        style = TextStyle(textAlign = TextAlign.Justify, fontSize = 15.sp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Note(
                note = dream.metadata.note,
                saveNote = { saveMetadata(dream.metadata.copy(note = it)) })
            TagRow(
                icon = Icons.Default.LocalOffer,
                tags = dream.metadata.tags,
                contentDescription = stringResource(id = R.string.tags),
                onAddTag = {
                    saveMetadata(dream.metadata.copy(tags = dream.metadata.tags + it))
                },
                onDeleteTag = {
                    saveMetadata(dream.metadata.copy(tags = dream.metadata.tags - it))
                },
                suggestions = tagsSuggestions
            )
            TagRow(
                icon = Icons.Default.People,
                tags = dream.metadata.peoples,
                contentDescription = stringResource(id = R.string.peoples),
                onAddTag = {
                    saveMetadata(dream.metadata.copy(peoples = dream.metadata.peoples + it))
                },
                onDeleteTag = {
                    saveMetadata(dream.metadata.copy(peoples = dream.metadata.peoples - it))
                },
                suggestions = peoplesSuggestions
            )
        }
    }
}

@Composable
fun TagRow(
    icon: ImageVector,
    tags: List<String>,
    contentDescription: String,
    onAddTag: (tag: String) -> Unit,
    onDeleteTag: (tag: String) -> Unit,
    suggestions: List<String>
) {
    val showTagAlertDialog = rememberSaveable {
        mutableStateOf(false)
    }

    val tag = rememberSaveable(showTagAlertDialog.value) {
        mutableStateOf("")
    }

    if (showTagAlertDialog.value) {
        AlertDialog(onDismissRequest = { showTagAlertDialog.value = false }, buttons = {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AutocompleteTextField(
                        text = tag.value,
                        onChangeText = {
                            tag.value = it
                        },
                        suggestions = suggestions,
                        onSelectSuggestion = {
                            tag.value = ""
                            onAddTag(it)
                        })
                    FlowRow {
                        tags.forEach {
                            Chip {
                                Row {
                                    Text(it)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = stringResource(id = R.string.delete),
                                        modifier = Modifier.clickable {
                                            onDeleteTag(it)
                                        })
                                }
                            }
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = {
                        onAddTag(tag.value)
                        showTagAlertDialog.value = false
                    }) {
                        Text(stringResource(id = R.string.add))
                    }
                }
            }
        })
    }
    FlowRow {
        Chip(modifier = Modifier.clickable {
            showTagAlertDialog.value = true
        }) {
            Icon(icon, contentDescription = contentDescription)
        }
        tags.forEach {
            Chip {
                Text(it)
            }
        }
    }
}

@Composable
fun Note(note: Int, saveNote: (value: Int) -> Unit) {
    val showAlertNote = rememberSaveable {
        mutableStateOf(false)
    }
    if (showAlertNote.value) {
        NoteAlertDialog(
            note = note,
            onDismissRequest = { showAlertNote.value = false },
            saveNote = {
                saveNote(it)
            })
    }
    Row {
        Chip(modifier = Modifier.clickable { showAlertNote.value = true }) {
            Icon(Icons.Default.Assignment, contentDescription = stringResource(id = R.string.note))
        }
        Chip {
            Text("$note")
        }
    }
}

@Composable
fun NoteAlertDialog(note: Int, onDismissRequest: () -> Unit, saveNote: (value: Int) -> Unit) {
    val sliderValue = rememberSaveable {
        mutableStateOf(note)
    }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Note: ${sliderValue.value}") },
        buttons = {
            Column {
                Slider(
                    value = sliderValue.value.toFloat(),
                    steps = 3,
                    valueRange = 0f..4f,
                    onValueChange = { sliderValue.value = it.toInt() })
            }
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = {
                    saveNote(sliderValue.value)
                    onDismissRequest()
                }) {
                    Text(stringResource(id = R.string.ok))
                }
            }
        }
    )

}

@Composable
fun Chip(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)) {
            content()
        }
    }
}

@Composable
private fun annotatedDream(text: String, word: String?): AnnotatedString {
    var start = if (word != null) text.indexOf(word, ignoreCase = true) else -1
    return buildAnnotatedString {
        append(text)
        while (word != null && word.isNotBlank() && start >= 0) {
            val end = start + word.length
            addStyle(
                SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colors.primary),
                start,
                end
            )
            start = text.indexOf(word, ignoreCase = true, startIndex = end)
        }
        toAnnotatedString()
    }
}