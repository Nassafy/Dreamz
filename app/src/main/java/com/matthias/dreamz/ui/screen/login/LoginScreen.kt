package com.matthias.dreamz.ui.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.matthias.dreamz.R

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(id = R.string.login)) }) },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.size(8.dp))
            TextField(
                value = loginViewModel.username,
                onValueChange = { loginViewModel.username = it },
                label = { Text(stringResource(id = R.string.username)) })
            TextField(
                value = loginViewModel.password,
                onValueChange = { loginViewModel.password = it },
                label = { Text(stringResource(id = R.string.password)) },
                visualTransformation = if (loginViewModel.passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (loginViewModel.passwordVisibility)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = {
                        loginViewModel.togglePasswordVisibility()
                    }) {
                        Icon(imageVector  = image, "")
                    }
                }
            )
            Button(
                onClick = { loginViewModel.login() },
                enabled = loginViewModel.username.isNotBlank() && loginViewModel.password.isNotBlank()
            ) {
                Text(stringResource(id = R.string.login), style = MaterialTheme.typography.h6)
            }
            if (loginViewModel.loginError) {
                Text(stringResource(id = R.string.login_error))
            }
        }
    }
    if (loginViewModel.loginSuccessfull) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
    }
}