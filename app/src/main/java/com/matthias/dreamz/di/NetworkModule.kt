package com.matthias.dreamz.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.matthias.dreamz.datastore.SettingsDataStoreManager
import com.matthias.dreamz.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        settingsDataStoreManager: SettingsDataStoreManager,
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                var newRequest: Request? = null
                runBlocking {
                    try {
                        val jwt = settingsDataStoreManager.getJwtToken().first()
                        newRequest =
                            it.request().newBuilder().addHeader("Authorization", "Bearer $jwt")
                                .build()
                    } catch (error: Exception) {
                        print(error)
                    }
                }
                val resp = it.proceed(newRequest ?: original)
                resp
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val baseUrl = "https://dreamz-gapi-ybbln.ondigitalocean.app"

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter().nullSafe())
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter().nullSafe())
            .create()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .build()
    }
}

private class LocalDateAdapter : TypeAdapter<LocalDate?>() {
    override fun write(jsonWriter: JsonWriter, localDate: LocalDate?) {
        if (localDate == null) {
            jsonWriter.nullValue()
        } else {
            val format = "yyyy-MM-dd'T'HH:mm:ss'Z'"
            jsonWriter.value(localDate.atStartOfDay().format(DateTimeFormatter.ofPattern(format)))
        }
    }

    override fun read(jsonReader: JsonReader): LocalDate? {
        return if (jsonReader.peek() === JsonToken.NULL) {
            jsonReader.nextNull()
            null
        } else {
            val dateStr = jsonReader.nextString()
            val format =
                if (dateStr.length == 20)
                    "yyyy-MM-dd'T'HH:mm:ss'Z'"
                else
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

            LocalDate.parse(
                dateStr, DateTimeFormatter.ofPattern(format)
            )
        }
    }
}

private class LocalDateTimeAdapter : TypeAdapter<LocalDateTime?>() {
    override fun write(jsonWriter: JsonWriter, localDate: LocalDateTime?) {
        if (localDate == null) {
            jsonWriter.nullValue()
        } else {
            val format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            jsonWriter.value(localDate.format(DateTimeFormatter.ofPattern(format)))
        }
    }

    override fun read(jsonReader: JsonReader): LocalDateTime? {
        return if (jsonReader.peek() === JsonToken.NULL) {
            jsonReader.nextNull()
            null
        } else {
            val dateStr = jsonReader.nextString()
            val format =
                if (dateStr.length == 20)
                    "yyyy-MM-dd'T'HH:mm:ss'Z'"
                else
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

            LocalDateTime.parse(
                dateStr, DateTimeFormatter.ofPattern(format)
            )
        }
    }
}