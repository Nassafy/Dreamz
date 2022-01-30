package com.matthias.dreamz.ui.screen.dreamlist.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.matthias.dreamz.R
import com.matthias.dreamz.ui.screen.viewdream.widget.Chip
import com.matthias.dreamz.ui.widget.AutocompleteTextField

@Composable
fun DreamSearch(
    filterText: String?,
    setFilterText: (String) -> Unit,
    filterTag: String?,
    setFilterTag: (String) -> Unit,
    tags: List<String>,
    filterPeople: String?,
    setFilterPeople: (String) -> Unit,
    minNote: Int?,
    setMinNote: (Int?) -> Unit,
    maxNote: Int?,
    setMaxNote: (Int?) -> Unit,
    peoples: List<String>
) {
    var openTextSearchDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var textSearch by rememberSaveable {
        mutableStateOf(filterText ?: "")
    }

    var tagSearch by rememberSaveable {
        mutableStateOf(filterTag ?: "")
    }

    var peopleSearch by rememberSaveable {
        mutableStateOf(filterPeople ?: "")
    }

    var minNoteSearch by rememberSaveable {
        mutableStateOf(
            minNote?.toString() ?: ""
        )
    }

    var maxNoteSearch by rememberSaveable {
        mutableStateOf(
            maxNote?.toString() ?: ""
        )
    }

    if (filterText?.isNotBlank() == true) {
        Chip {
            Row {
                Text(text = filterText)
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Delete",
                    modifier = Modifier.clickable {
                        setFilterText("")
                    })
            }
        }
    }

    if (filterTag?.isNotBlank() == true) {
        Chip {
            Row {
                Text(text = filterTag)
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Delete",
                    modifier = Modifier.clickable {
                        setFilterTag("")
                    })
            }
        }
    }
    if (filterPeople?.isNotBlank() == true) {
        Chip {
            Row {
                Text(text = filterPeople)
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Delete",
                    modifier = Modifier.clickable {
                        setFilterPeople("")
                    })
            }
        }
    }
    if (minNote != null) {
        Chip {
            Row {
                Text(text = "> $minNote")
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Delete",
                    modifier = Modifier.clickable {
                        setMinNote(null)
                    })
            }
        }
    }

    if (maxNote != null) {
        Chip {
            Row {
                Text(text = "< $maxNote")
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Delete",
                    modifier = Modifier.clickable {
                        setMaxNote(null)
                    })
            }
        }
    }
    IconButton(onClick = { openTextSearchDialog = true }) {
        Icon(Icons.Default.Search, contentDescription = "Search")
    }
    if (openTextSearchDialog) {
        AlertDialog(onDismissRequest = {
            openTextSearchDialog = false
        }, buttons = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextButton(onClick = {
                    setFilterText(textSearch)
                    setFilterTag(tagSearch)
                    setFilterPeople(peopleSearch)
                    setMinNote(minNoteSearch.toIntOrNull())
                    setMaxNote(maxNoteSearch.toIntOrNull())
                    textSearch = ""
                    tagSearch = ""
                    peopleSearch = ""
                    minNoteSearch = ""
                    maxNoteSearch = ""
                    openTextSearchDialog = false
                }) {
                    Text(stringResource(id = R.string.search))
                }
            }
        }, text = {
            Column {
                OutlinedTextField(value = textSearch, onValueChange = { textSearch = it }, label = {
                    Text(
                        text = stringResource(id = R.string.search)
                    )
                })
                AutocompleteTextField(
                    text = tagSearch,
                    onChangeText = { tagSearch = it },
                    onSelectSuggestion = { tagSearch = it },
                    suggestions = tags,
                    label = {
                        Text(text = stringResource(id = R.string.tag))
                    }
                )
                AutocompleteTextField(
                    text = peopleSearch,
                    onChangeText = { peopleSearch = it },
                    onSelectSuggestion = { peopleSearch = it },
                    suggestions = peoples,
                    label = {
                        Text(text = stringResource(id = R.string.people))
                    }
                )
                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    OutlinedTextField(
                        value = minNoteSearch,
                        onValueChange = {
                            minNoteSearch = it
                        },
                        label = {
                            Text(text = "Min note")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(fraction = 0.45f)
                    )
                    Spacer(modifier = Modifier.fillMaxWidth(fraction = (1f * 1f / 6f)))
                    OutlinedTextField(
                        value = maxNoteSearch,
                        onValueChange = {
                            maxNoteSearch = it
                        },
                        label = {
                            Text(text = "Max note")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(fraction = 1f)
                    )
                }
            }
        }
        )
    }

}