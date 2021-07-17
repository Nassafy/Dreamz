package com.matthias.dreamz.ui.screen.editdream

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.matthias.dreamz.ui.screen.Screen
import com.matthias.dreamz.ui.screen.editdream.widget.EditDream

@ExperimentalComposeUiApi
@Composable
fun EditDreamScreen(
    editDreamViewModel: EditDreamViewModel,
    dreamId: Long,
    navController: NavController
) {
    val scrollableState = rememberScrollState(0)
    val dreamDay = editDreamViewModel.getDream(dreamId).collectAsState(initial = null).value

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = dreamDay?.date ?: "") }, actions = {
            IconButton(onClick = {
                navController.popBackStack()
                navController.navigate(Screen.ViewDream.createRoute(dreamId))
            }) {
                Icon(Icons.Default.RemoveRedEye, contentDescription = "View")
            }
            if (dreamDay?.dreams?.isEmpty() == true) {
                IconButton(onClick = {
                    editDreamViewModel.deleteDreamDay(dreamId)
                    navController.popBackStack(Screen.DreamList.route, inclusive = false)
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        })
    }) {
        if (dreamDay != null) {
            Column(
                modifier = Modifier
                    .verticalScroll(
                        scrollableState,
                    )
                    .navigationBarsWithImePadding()
            ) {
                (dreamDay.dreams).forEach { dreamState ->
                    EditDream(
                        dream = dreamState,
                        save = { editDreamViewModel.saveDream(it) },
                        delete = { editDreamViewModel.deleteDream(it) })
                }
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = { editDreamViewModel.addDream(dreamDay.id) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add dream")
                    }
                }
            }
        }
    }
}