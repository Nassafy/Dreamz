package com.matthias.dreamz.repository

import androidx.lifecycle.viewModelScope
import com.matthias.dreamz.api.AuthApi
import com.matthias.dreamz.api.AuthDto
import com.matthias.dreamz.datastore.SettingsDataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.*
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val authDataStoreManager: SettingsDataStoreManager,
    private val authApi: AuthApi
) {
    val logged = authDataStoreManager.getJwtToken().map { it != null && it.isNotBlank() }
    val jwt = authDataStoreManager.getJwtToken()

    suspend fun login(username: String, password: String) = withContext<Boolean>(Dispatchers.IO) {
        try {
            val resp = authApi.auth(AuthDto(username = username, password = password)).await()
            val jwt = resp.string()
            authDataStoreManager.writeJwtToken(jwtToken = jwt)
            true
        } catch (httpException: HttpException) {
            false
        }

    }

}