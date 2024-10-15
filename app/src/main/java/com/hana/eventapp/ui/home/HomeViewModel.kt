package com.hana.eventapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hana.eventapp.data.response.ListEventResponse
import com.hana.eventapp.data.response.ListEventsItem
import com.hana.eventapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> get() = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> get() = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    companion object {
        private const val TAG = "HomeViewModel"
    }

    fun getHomeUpcomingEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUpcoming()
        client.enqueue(object : Callback<ListEventResponse> {
            override fun onResponse(
                call: Call<ListEventResponse>,
                response: Response<ListEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(
                        "Home-UpcomingFragment",
                        "Number of events: ${response.body()?.listEvents?.size}"
                    )
                    _upcomingEvents.value = response.body()?.listEvents?.take(5)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getHomeFinishedEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFinished()
        client.enqueue(object : Callback<ListEventResponse> {
            override fun onResponse(
                call: Call<ListEventResponse>,
                response: Response<ListEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(
                        "Home-FinishedFragment",
                        "Number of events: ${response.body()?.listEvents?.size}"
                    )
                    _finishedEvents.value = response.body()?.listEvents?.take(5)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(p0: Call<ListEventResponse>, p1: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${p1.message.toString()}")
            }
        })
    }
}