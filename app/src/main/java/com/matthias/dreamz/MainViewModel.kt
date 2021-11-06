package com.matthias.dreamz

import androidx.lifecycle.ViewModel
import com.matthias.dreamz.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val logged = authRepository.logged
}