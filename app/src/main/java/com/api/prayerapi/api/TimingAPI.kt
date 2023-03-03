package com.api.prayerapi.api

import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.api.prayerapi.response.TimePrayer
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TimingAPI {

    @GET("timingsByCity/:date?country=SA&")
    suspend fun getDataPrayer(
        @Query("city") city: String
    ): TimePrayer
}