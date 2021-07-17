package com.matthias.dreamz.ui.widget

import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties

@Composable
fun AutocompleteTextField(
    text: String,
    onChangeText: (text: String) -> Unit,
    onSelectSuggestion: (suggestion: String) -> Unit,
    suggestions: List<String>,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
) {

    var showSuggestion by rememberSaveable {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = text,
        onValueChange = {
            onChangeText(it)
            showSuggestion = true
        },
        modifier = modifier,
        label = label
    )
    DropdownMenu(
        modifier = Modifier.requiredHeightIn(max = 200.dp),
        expanded = showSuggestion,
        properties = PopupProperties(focusable = false),
        onDismissRequest = { showSuggestion = false }) {
        suggestions.filter { it.startsWith(text, ignoreCase = true) }.forEach {
            DropdownMenuItem(onClick = {
                showSuggestion = false
                onSelectSuggestion(it)
            }) {
                Text(it)
            }
        }
    }
}