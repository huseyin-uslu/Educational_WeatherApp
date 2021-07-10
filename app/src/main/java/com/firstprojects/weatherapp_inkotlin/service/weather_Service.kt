package com.firstprojects.weatherapp_inkotlin.service

import com.firstprojects.weatherapp_inkotlin.model.Weather
import com.google.gson.Gson
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class weather_Service {


  private val BASE_URL = "https://api.openweathermap.org/"

  private val retrofit = Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .baseUrl(BASE_URL)
      .build()
      .create(weather_api::class.java)

    fun getData(city : String) : Single<Weather> {
        return retrofit.getData(city)
    }

}