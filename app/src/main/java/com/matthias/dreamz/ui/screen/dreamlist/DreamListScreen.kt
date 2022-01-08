package com.matthias.dreamz.ui.screen.dreamlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.matthias.dreamz.R
import com.matthias.dreamz.data.model.TagType
import com.matthias.dreamz.ui.screen.Screen
import com.matthias.dreamz.ui.screen.dreamlist.widget.DreamCard
import com.matthias.dreamz.ui.screen.dreamlist.widget.DreamSearch
import com.matthias.dreamz.ui.screen.dreamlist.widget.DreamzDrawer
import com.matthias.dreamz.ui.screen.viewdream.widget.Chip
import kotlinx.coroutines.launch

@Composable
fun DreamsListScreen(
    dreamListViewModel: DreamListViewModel,
    navController: NavController,
) {
    val coroutineScope = rememberCoroutineScope()
    val state = rememberLazyListState()

    val refreshing by dreamListViewModel.refreshing.observeAsState(false)
    val swipeState = rememberSwipeRefreshState(refreshing)
    val filterText = dreamListViewModel.filterText.collectAsState(initial = null).value

    val todayDream = dreamListViewModel.todayDream.collectAsState(initial = null).value
    val dreamDays = dreamListViewModel.dreamDays.collectAsState(initial = null).value
    val scaffoldState = rememberScaffoldState()

    val tags =
        dreamListViewModel.getSuggestions(TagType.TAG).collectAsState(initial = listOf()).value
    val filterTag = dreamListViewModel.filterTag.collectAsState(initial = "").value

    val peoples =
        dreamListViewModel.getSuggestions(TagType.PEOPLE).collectAsState(initial = listOf()).value
    val filterPeople = dreamListViewModel.filterPeople.collectAsState(initial = "").value

    val syncState = dreamListViewModel.syncState.collectAsState(initial = true).value

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.app_name)) }, actions = {
                DreamSearch(
                    filterText = filterText,
                    setFilterText = { dreamListViewModel.setFilterText(it) },
                    filterTag = filterTag,
                    setFilterTag = {
                        dreamListViewModel.setFilterTag(it)
                    },
                    tags = tags,
                    filterPeople = filterPeople,
                    setFilterPeople = {
                        dreamListViewModel.setFilterPeople(it)
                    },
                    peoples = peoples,
                )
                if (dreamDays != null && dreamDays.isEmpty()) {
                    IconButton(onClick = { dreamListViewModel.sync() }) {
                        Icon(Icons.Default.Sync, contentDescription = stringResource(id = R.string.sync))
                    }
                }
                if (!syncState) {
                    Icon(Icons.Default.SyncDisabled, contentDescription = stringResource(id = R.string.sync_failed))
                }
                Chip {
                    Text("${dreamDays?.size ?: 0}")
                }
            }, navigationIcon = {
                IconButton(onClick = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = stringResource(id = R.string.drawer)
                    )
                }
            })
        },
        floatingActionButton = {
            if (todayDream == null) {
                FloatingActionButton(
                    onClick = {
                        dreamListViewModel.addDream(
                            onAdd = {
                                coroutineScope.launch {
                                    navController.navigate(
                                        Screen.EditDream.createRoute(
                                            it
                                        )
                                    )
                                }
                            }
                        )
                    }) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add))
                }
            } else {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(
                            Screen.EditDream.createRoute(
                                todayDream.uid
                            )
                        )
                    }) {
                    Icon(Icons.Default.Edit, contentDescription = stringResource(id = R.string.edit))
                }
            }

        },
        drawerContent = {
            DreamzDrawer(
                navController = navController,
                drawerState = scaffoldState.drawerState
            )
        }, scaffoldState = scaffoldState
    ) {
        SwipeRefresh(
            state = swipeState,
            onRefresh = {
                if (!refreshing) {
                    dreamListViewModel.sync()
                }
            }) {
            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                state = state

            ) {
                items(dreamDays ?: listOf(), key = { dream -> dream.id }) { dream ->
                    DreamCard(dreamDay = dream, modifier = Modifier.clickable {
                        navController.navigate(Screen.ViewDream.createRoute(dream.id))
                    })

                }
            }
        }
    }
}