package com.firstprojects.weatherapp_inkotlin.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firstprojects.weatherapp_inkotlin.model.Weather
import com.firstprojects.weatherapp_inkotlin.service.weather_Service
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainModelView : ViewModel(){

    private val weather = weather_Service()
    private val disposable = CompositeDisposable()

    val data   = MutableLiveData<Weather>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()

    fun refreshData(cityName : String) {
        getDataFromAPI(cityName)
    }

    private fun getDataFromAPI(cityName : String) {
        loading.value = true
        disposable.add(
            weather.getData(cityName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Weather>() {
                    override fun onSuccess(t: Weather) {
                        loading.value = false
                        data.value = t
                        error.value = false
                        Log.i("MainModelView","data has come succesfully.")
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        error.value = true
                        Log.w("MainModelView",e.localizedMessage!!.toString())
                    }
                })
        )
    }


}