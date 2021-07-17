package com.matthias.dreamz.ui.screen.graph

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.matthias.dreamz.ui.screen.graph.widget.DreamGraph
import java.time.LocalDate

@ExperimentalPagerApi
@Composable
fun GraphScreen(graphViewModel: GraphViewModel) {
    val currentYear = LocalDate.now().year
    val pagerState = rememberPagerState(initialPage = currentYear)
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Graph") })
    }) {
        Surface(color = MaterialTheme.colors.surface, modifier = Modifier.fillMaxSize()) {
            HorizontalPager(count = currentYear + 1, state = pagerState) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.size(60.dp))
                    Text("$it")
                    Spacer(modifier = Modifier.size(40.dp))
                    DreamGraph(
                        year = it,
                        getDream = { year -> graphViewModel.getDreams(year) })
                }
            }
        }
    }
}