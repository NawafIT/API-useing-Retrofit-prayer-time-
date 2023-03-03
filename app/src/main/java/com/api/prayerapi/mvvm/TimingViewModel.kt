package com.api.prayerapi.mvvm

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.api.prayerapi.response.Data
import com.api.prayerapi.response.TimePrayer
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class TimingViewModel(
    private val timingRepository: TimingRepository = TimingRepository(),

    ) :
    ViewModel() {
    val cityState = MutableLiveData("Jeddah")
    val myObject: MutableLiveData<TimePrayer> = MutableLiveData(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            myObject.postValue(getDataPrayer())
        }

    }

    private suspend fun getDataPrayer(
        city: String = cityState.value!!
    ): TimePrayer {
        return timingRepository.getDataPrayer(city)
    }

    fun ChangeTheCity(city: String) {
        val newCity = when (city) {
            "جدة" -> {
                "Jeddah"
            }
            "الرياض" -> {
                "Ar Riyāḑ"
            }
            "المدينة المنورة" -> {
                "Al Madīnah al Munawwarah"
            }
            else -> {
                "Ash Sharqīyah"
            }
        }
        cityState.value = newCity

        viewModelScope.launch(Dispatchers.IO) {
            myObject.postValue(getDataPrayer())
        }
    }
}