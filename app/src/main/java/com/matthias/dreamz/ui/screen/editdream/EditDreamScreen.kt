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
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.matthias.dreamz.ui.screen.Screen
import com.matthias.dreamz.ui.screen.editdream.widget.EditDream
import com.matthias.dreamz.ui.widget.BackNavButton
import com.matthias.dreamz.widget.DreamzWidgetReceiver.Companion.updateWidgets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun EditDreamScreen(
    editDreamViewModel: EditDreamViewModel,
    dreamId: Long,
    navController: NavController
) {
    val scrollableState = rememberScrollState(0)
    val dreamDay = editDreamViewModel.getDream(dreamId).collectAsState(initial = null).value
    val coroutine = rememberCoroutineScope();
    val context = LocalContext.current

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
                    editDreamViewModel.deleteDreamDay(dreamDay.uuid, dreamId, afterDelete = {
                        coroutine.launch(Dispatchers.IO) { context.updateWidgets() }
                        navController.popBackStack(Screen.DreamList.route, inclusive = false)
                    })
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }, navigationIcon = {
            BackNavButton(navController)
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
                        save = {
                            editDreamViewModel.saveDream(it)
                            coroutine.launch(Dispatchers.IO) { context.updateWidgets() }
                        },
                        delete = {
                            editDreamViewModel.deleteDream(it)
                        })
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