package com.matthias.dreamz.ui.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Login") }) }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(40.dp),modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.size(30.dp))
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
                Text("Login", style = MaterialTheme.typography.h6)
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