package com.api.prayerapi.api

import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.api.prayerapi.api.Constant.BASE_URL
import com.api.prayerapi.response.TimePrayer
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WebAPI {
    private var api: TimingAPI

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(TimingAPI::class.java)
    }


    suspend fun getDataPrayer(city: String): TimePrayer {
        return api.getDataPrayer(city)
    }
}