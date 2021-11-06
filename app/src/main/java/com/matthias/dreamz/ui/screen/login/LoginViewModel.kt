package com.matthias.dreamz.ui.screen.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matthias.dreamz.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var loginError by mutableStateOf(false)
        private set
    var loginSuccessfull by mutableStateOf(false)
        private set

    fun login() {
        viewModelScope.launch {
            loginError = !authRepository.login(username = username, password = password)
            if (!loginError) {
                loginSuccessfull = true
            }
        }
    }
}