package com.example.myreactivesearchcoroutineflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.myreactivesearchcoroutineflow.remote.network.ApiConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel : ViewModel() {

    private val accessToken = "pk.eyJ1IjoiYXJpZmFpemluIiwiYSI6ImNrYTI2c3R1cjAwNXAzbm1zaDYyZW1ra2cifQ.okSWF0zf58rWkhoVuYjShQ"
    val queryChannel = MutableStateFlow("") // menyimpan state data : selalu membaca value

    val searchResult = queryChannel
        .debounce(300) // eksekusi selanjutnya berjalan jika ada jeda
        .distinctUntilChanged() //  Berfungsi kala request sebelumnya dengan sekarang, sama. Jadi, tidak perlu melakukan request lagi.
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest { // mengganti data lama dengan data baru
            ApiConfig.provideApiService().getCountry(it, accessToken).features
        }
        .asLiveData()
}