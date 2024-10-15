package com.hana.eventapp.ui.finished

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

class FinishedViewModel : ViewModel() {

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    companion object{
        private const val TAG = "FinishedViewModel"
    }

    fun getFinishedEvents() {
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
                        "FinishedFragment",
                        "Number of events: ${response.body()?.listEvents?.size}"
                    )
                    _events.value = response.body()?.listEvents
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
}

private fun <T> Call<T>.enqueue(callback: Callback<ListEventResponse>) {

}
