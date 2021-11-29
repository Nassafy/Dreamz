package com.matthias.dreamz.ui.screen.dreamlist.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
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
            ) {
                TextButton(onClick = {
                    setFilterText(textSearch)
                    setFilterTag(tagSearch)
                    setFilterPeople(peopleSearch)
                    textSearch = ""
                    tagSearch = ""
                    peopleSearch = ""
                    openTextSearchDialog = false
                }) {
                    Text("Search")
                }
            }
        }, text = {
            Column {
                OutlinedTextField(value = textSearch, onValueChange = { textSearch = it }, label = {
                    Text(
                        text = "Search"
                    )
                })
                Spacer(modifier = Modifier.size(10.dp))
                AutocompleteTextField(
                    text = tagSearch,
                    onChangeText = { tagSearch = it },
                    onSelectSuggestion = { tagSearch = it },
                    suggestions = tags,
                    label = {
                        Text(text = "Tag")
                    }
                )
                Spacer(modifier = Modifier.size(10.dp))
                AutocompleteTextField(
                    text = peopleSearch,
                    onChangeText = { peopleSearch = it },
                    onSelectSuggestion = { peopleSearch = it },
                    suggestions = peoples,
                    label = {
                        Text(text = "People")
                    }
                )
            }
        }
        )
    }

}