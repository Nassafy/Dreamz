package com.matthias.dreamz.ui.screen.tags

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.matthias.dreamz.R
import com.matthias.dreamz.data.model.TagType
import com.matthias.dreamz.ui.screen.Screen
import com.matthias.dreamz.ui.widget.BackNavButton

@Composable
fun TagsScreen(tagsViewModel: TagsViewModel, tagType: TagType, navController: NavController) {
    val tags = tagsViewModel.getTagsInfo(tagType).collectAsState(initial = listOf()).value
    val scrollState = rememberScrollState()
    val tagName = when (tagType) {
        TagType.TAG -> stringResource(id = R.string.tag)
        TagType.PEOPLE -> stringResource(id = R.string.people)
    }
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = tagName) }, navigationIcon = {
            BackNavButton(navController)
        })
    }) {
        Surface(elevation = 5.dp, modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .verticalScroll(scrollState)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        tagName,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(horizontal = 15.dp)
                    )
                    Text(
                        stringResource(id = R.string.count),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(horizontal = 15.dp)
                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Divider()
                Spacer(modifier = Modifier.size(10.dp))
                tags.forEach {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clickable {
                                tagsViewModel.setFilter(tagType = tagType, tag = it.name)
                                navController.navigate(Screen.DreamList.route)

                            }
                    ) {
                        Text(it.name, modifier = Modifier.padding(horizontal = 15.dp))
                        Text("${it.count}", modifier = Modifier.padding(horizontal = 15.dp))
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Divider()
                    Spacer(modifier = Modifier.size(10.dp))
                }
            }
        }
    }
}