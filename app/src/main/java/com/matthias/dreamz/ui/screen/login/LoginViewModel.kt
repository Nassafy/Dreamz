package com.matthias.dreamz.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.matthias.dreamz.repository.AuthRepository
import com.matthias.dreamz.worker.SyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository, private val workManager: WorkManager) : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVisibility by  mutableStateOf(false)
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

    fun togglePasswordVisibility() {
        passwordVisibility = !passwordVisibility
    }

    fun sync() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val work = OneTimeWorkRequestBuilder<SyncWorker>().setConstraints(
            constraints
        ).setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST).addTag(SyncWorker.TAG)
            .build()
        workManager.enqueueUniqueWork(SyncWorker.NAME_UNIQUE, ExistingWorkPolicy.KEEP, work)
    }
}