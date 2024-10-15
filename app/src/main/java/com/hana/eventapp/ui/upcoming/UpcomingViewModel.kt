package com.hana.eventapp.ui.upcoming

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
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

@SuppressLint("ParcelCreator")
class UpcomingViewModel() : ViewModel(), Parcelable {

    // LiveData untuk menyimpan daftar event
    private val _events = MutableLiveData<List<ListEventsItem>?>()
    val events: MutableLiveData<List<ListEventsItem>?> get() = _events

    // LiveData untuk menandai loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Konstruktor untuk Parcelable
    constructor(parcel: Parcel) : this()

    companion object {
        private const val TAG = "UpcomingViewModel"

        // CREATOR untuk Parcelable
        @JvmField
        val CREATOR: Parcelable.Creator<UpcomingViewModel> = object : Parcelable.Creator<UpcomingViewModel> {
            override fun createFromParcel(parcel: Parcel): UpcomingViewModel {
                return UpcomingViewModel(parcel)
            }

            override fun newArray(size: Int): Array<UpcomingViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    // Fungsi untuk mengambil daftar event dari API
    fun getUpcomingEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUpcoming()
        client.enqueue(object : Callback<ListEventResponse> {
            override fun onResponse(
                call: Call<ListEventResponse>,
                response: Response<ListEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val eventsList = response.body()?.listEvents
                    Log.d(TAG, "Number of events: ${eventsList?.size ?: 0}")
                    _events.value = eventsList
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

    // Implementasi Parcelable
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        // Implementasikan logika penulisan ke parcel jika dibutuhkan
    }

    override fun describeContents(): Int {
        return 0
    }
}

private fun <T> Call<T>.enqueue(callback: Callback<ListEventResponse>) {
    TODO("Not yet implemented")
}
