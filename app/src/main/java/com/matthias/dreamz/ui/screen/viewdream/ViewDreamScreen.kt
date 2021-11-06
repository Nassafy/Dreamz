package com.matthias.dreamz.ui.screen.viewdream

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.matthias.dreamz.data.model.TagType
import com.matthias.dreamz.ui.screen.Screen
import com.matthias.dreamz.ui.screen.viewdream.widget.DreamView

@ExperimentalPagerApi
@Composable
fun ViewDreamScreen(
    viewDreamViewModel: ViewDreamViewModel,
    dreamId: Long,
    navController: NavController
) {
    val dreamDays = viewDreamViewModel.getDreams().collectAsState(initial = listOf()).value
    val currentDream = dreamDays.firstOrNull { it.uid == dreamId }
    val scrollState = rememberScrollState()
    val index = if (currentDream != null) dreamDays.indexOf(currentDream) else 0
    val pagerState = when (dreamDays.isEmpty()) {
        true -> null
        false -> rememberPagerState(initialPage = index)
    }
    val peoplesSuggestions =
        viewDreamViewModel.getSuggestions(tagType = TagType.PEOPLE).collectAsState(
            initial = listOf()
        ).value
    val tagsSuggestions =
        viewDreamViewModel.getSuggestions(tagType = TagType.TAG).collectAsState(
            initial = listOf()
        ).value
    val searchText = viewDreamViewModel.searchText.collectAsState(initial = null).value

    Scaffold(topBar = {
        TopAppBar(title = {
            if (dreamDays.isNotEmpty() && pagerState != null) {
                Text(dreamDays[pagerState.currentPage].toState().date)
            }
        }, actions = {
            IconButton(onClick = {
                navController.popBackStack()
                navController.navigate(Screen.EditDream.createRoute(dreamId))
            }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        })
    }) {
        Column(modifier = Modifier.fillMaxHeight()) {
            if (pagerState != null) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .verticalScroll(scrollState),
                    verticalAlignment = Alignment.Top,
                    count = dreamDays.size,
                ) { page ->
                    val dreamDay = viewDreamViewModel.getDream(dreamDays[page].uid)
                        .collectAsState(initial = null).value
                    Column {
                        dreamDay?.dreams?.forEach { dreamState ->
                            DreamView(
                                dream = dreamState, saveMetadata = {
                                    viewDreamViewModel.saveDreamMetadata(
                                        dreamId = dreamState.id,
                                        metadata = it
                                    )
                                },
                                tagsSuggestions = tagsSuggestions,
                                peoplesSuggestions = peoplesSuggestions,
                                searchText = searchText
                            )
                        }

                    }
                }
            }
        }
    }
}