package com.matthias.dreamz.ui.widget

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BackNavButton(navController: NavController) {
    IconButton(onClick = { navController.popBackStack() }) {
        Icon(Icons.Default.ArrowBack, "Back")
    }
}