package com.firstprojects.weatherapp_inkotlin.service

import com.firstprojects.weatherapp_inkotlin.model.Weather
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface weather_api {
    //LINK = https://api.openweathermap.org/data/2.5/weather?q=kutahya&APPID=04a42b96398abc8e4183798ed22f9485
    //BASE_URL = https://api.openweathermap.org/data/2.5/
    //KEY = weather?q=kutahya&APPID=04a42b96398abc8e4183798ed22f9485

    @GET("data/2.5/weather?&units=metric&APPID=04a42b96398abc8e4183798ed22f9485")
    fun getData(
        @Query("q")cityName: String
    ) : Single<Weather>
}