package com.matthias.dreamz.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.matthias.dreamz.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    val logged = authRepository.logged

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}