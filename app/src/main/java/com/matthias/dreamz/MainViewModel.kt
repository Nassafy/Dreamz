package com.matthias.dreamz

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matthias.dreamz.api.AuthApi
import com.matthias.dreamz.api.AuthDto
import com.matthias.dreamz.datastore.SettingsDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val authApi: AuthApi,
    private val dataStoreManager: SettingsDataStoreManager
) : ViewModel() {
    fun authenticate(context: Context) {
        authApi.auth(AuthDto(username = "matthias", password = "pass"))
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    //handle error here
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    //your raw string response
                    val jwt = response.body()?.string()
                    viewModelScope.launch() {
                        if (jwt != null) {
                            dataStoreManager.writeJwtToken(jwtToken = jwt)
                        }
                    }
                }
            })

    }
}