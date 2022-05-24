package com.matthias.dreamz.ui.screen.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.matthias.dreamz.R
import com.matthias.dreamz.ui.screen.Screen

@Composable
fun SettingScreen(navController: NavController, settingViewModel: SettingViewModel) {
    val logged = settingViewModel.logged.collectAsState(initial = false).value

    if (logged) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = { settingViewModel.logout() }) {
                Text(text = stringResource(id = R.string.logout))
            }
        }
    } else {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                Text(text = stringResource(id = R.string.login))
            }
        }
    }

}