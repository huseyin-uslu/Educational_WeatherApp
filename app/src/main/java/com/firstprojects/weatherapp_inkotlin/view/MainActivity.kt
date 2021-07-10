package com.firstprojects.weatherapp_inkotlin.view


import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.firstprojects.weatherapp_inkotlin.databinding.ActivityMainBinding
import com.firstprojects.weatherapp_inkotlin.viewmodel.MainModelView

class MainActivity : AppCompatActivity() {
    //declaration
     lateinit var vM : MainModelView
     var _binding : ActivityMainBinding? = null
     val binding get() = _binding!!

    //shared
   private lateinit var SP : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialization
        SP = getSharedPreferences(packageName,MODE_PRIVATE)
        vM = ViewModelProvider.NewInstanceFactory().create(MainModelView::class.java)

        val cName = SP.getString("cityName","Kutahya") ?: "Kutahya"
        binding.edtCityName.setText(cName)

        vM.refreshData(cName)
        getDataObservable()

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.llData.visibility = View.GONE
            binding.pbLoading.visibility = View.GONE
            binding.tvError.visibility = View.GONE

            val cityName = SP.getString("cityName",cName)
            vM.refreshData(cityName!!)
            binding.edtCityName.setText(cityName)

            binding.llData.visibility = View.VISIBLE
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.imgSearchCity.setOnClickListener {
            val cityName = binding.edtCityName.text.toString()
            SP.edit().putString("cityName",cityName).apply()
            vM.refreshData(cityName)
            getDataObservable()
            Log.i("mainActivity","SAVED the city name")


        }

    }


    private fun getDataObservable() {

        Log.i("mainActivity","onGetDataObservable")
        vM.data.observe(this) {
            Log.i("mainActivity","DATA NAME OF CİTY = ${it!!.name}")
            println("DATA NAME OF CİTY = ${it.name}")
           when(it){
               null -> binding.llData.visibility = View.GONE
               else ->  it.let {

                   binding.llData.visibility = View.VISIBLE
                   binding.tvCityCode.text = it.sys?.country
                   binding.tvCityName.text = it.name

                   val iconInt : String? = it.weather?.get(0)?.icon
                   Glide.with(this)
                       .load("https://openweathermap.org/img/wn/$iconInt@2x.png")
                       .into(binding.imgWeatherPictures)

                   binding.tvDegree.text = it.main!!.temp.toString() + "°C"
                   binding.tvHumidity.text = it.main!!.humidity.toString() + "%"
                   binding.tvWindSpeed.text = it.wind!!.speed.toString()
                   binding.tvLat.text = it.coord!!.lat.toString()
                   binding.tvLon.text = it.coord.lon.toString()
               }
           }

        }

        vM.error.observe(this){error ->
            if(error){
                binding.tvError.visibility   = View.VISIBLE
                binding.pbLoading.visibility = View.GONE
                binding.llData.visibility = View.GONE
            }else{
                binding.tvError.visibility = View.GONE
            }
        }

        vM.loading.observe(this){loading ->
            if(loading){
                binding.pbLoading.visibility = View.VISIBLE
                binding.tvError.visibility = View.GONE
                binding.llData.visibility = View.GONE
            }else{
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

