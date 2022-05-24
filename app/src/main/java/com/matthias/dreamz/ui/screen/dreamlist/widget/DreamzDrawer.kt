package com.matthias.dreamz.ui.screen.dreamlist.widget

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.matthias.dreamz.R
import com.matthias.dreamz.ui.screen.Screen
import kotlinx.coroutines.launch

@Composable
fun DreamzDrawer(navController: NavController, drawerState: DrawerState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        DrawerElem(
            route = Screen.Calendar.route,
            name = stringResource(id = R.string.calendar),
            icon = Icons.Default.CalendarToday,
            navController = navController,
            drawerState = drawerState
        )
        DrawerElem(
            route = Screen.Peoples.route,
            name = stringResource(id = R.string.peoples),
            icon = Icons.Default.People,
            navController = navController,
            drawerState = drawerState
        )
        DrawerElem(
            route = Screen.Tags.route,
            name = stringResource(id = R.string.tags),
            icon = Icons.Default.Tag,
            navController = navController,
            drawerState = drawerState
        )
        DrawerElem(
            route = Screen.Graph.route,
            name = stringResource(id = R.string.graphs),
            icon = Icons.Default.Leaderboard,
            navController = navController,
            drawerState = drawerState
        )
        DrawerElem(
            route = Screen.Setting.route,
            name = stringResource(id = R.string.setting),
            icon = Icons.Default.Settings,
            navController = navController,
            drawerState = drawerState
        )
    }
}

@Composable
fun DrawerElem(
    route: String,
    name: String,
    icon: ImageVector,
    navController: NavController,
    drawerState: DrawerState
) {
    val coroutineScope = rememberCoroutineScope()

    Row(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            onClick =
            {
                navController.navigate(route)
                coroutineScope.launch {
                    drawerState.close()
                }
            },
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colors.onBackground)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(icon, name)
                Spacer(modifier = Modifier.size(10.dp))
                Text(name)
            }
        }
    }
}

