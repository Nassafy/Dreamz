package com.matthias.dreamz.ui.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Login") }) }) {
        Column {
            Spacer(modifier = Modifier.size(50.dp))
            TextField(
                value = loginViewModel.username,
                onValueChange = { loginViewModel.username = it },
                label = { Text("Usernames") })
            TextField(
                value = loginViewModel.password,
                onValueChange = { loginViewModel.password = it },
                label = { Text("Password") }
            )
            Button(
                onClick = { loginViewModel.login() },
                enabled = loginViewModel.username.isNotBlank() && loginViewModel.password.isNotBlank()
            ) {
                Text("Login")
            }
            if (loginViewModel.loginError) {
                Text("Login Error")
            }
        }
    }
    if (loginViewModel.loginSuccessfull) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
    }
}