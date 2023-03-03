package com.api.prayerapi.mvvm

import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.api.prayerapi.api.WebAPI
import com.api.prayerapi.response.TimePrayer
import kotlinx.coroutines.flow.Flow

class TimingRepository(private val webApi: WebAPI = WebAPI()) {
    suspend fun getDataPrayer(city: String): TimePrayer {
        return webApi.getDataPrayer(city)
    }
}