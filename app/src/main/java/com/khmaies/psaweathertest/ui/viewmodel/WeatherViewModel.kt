package com.khmaies.psaweathertest.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khmaies.data.model.Coordination
import com.khmaies.data.model.WeatherDataResponse
import com.khmaies.data.model.WeatherDetail
import com.khmaies.data.repository.WeatherRepository
import com.khmaies.psaweathertest.ui.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel(private val weatherRepo: WeatherRepository) : ViewModel() {

    private val _weatherLiveData =
        MutableLiveData<WeatherDetail?>()
    val weatherLiveData: LiveData<WeatherDetail?>
        get() = _weatherLiveData

    private val _weatherDetailListLiveData =
        MutableLiveData<Event<State<MutableList<WeatherDetail>>>>()
    val weatherDetailListLiveData: LiveData<Event<State<MutableList<WeatherDetail>>>>
        get() = _weatherDetailListLiveData

    private lateinit var weatherResponse: WeatherDataResponse

    private lateinit var coordResponse: Coordination


    /**
     * sending two request to api
     * 1- to find city longitude&latitude
     * 2- use the result of first request to fetch weather data from api
     *
     * @param cityName
     */
    private fun findCityWeather(cityName: String) {
       // _weatherLiveData.postValue(Event(State.loading()))
        viewModelScope.launch {


            try {
                coordResponse = weatherRepo.findCityCoord(cityName)

                weatherResponse = weatherRepo.findCityWeather(coordResponse)
                weatherResponse.id = coordResponse.id
                weatherResponse.country = coordResponse.sys.country
                weatherResponse.name = coordResponse.name

                addWeatherDetailtoDb(weatherResponse)


                withContext(Dispatchers.Main) {
                    val weatherDetail = WeatherDetail()
                    weatherDetail.icon = weatherResponse.current?.weather?.first()?.icon
                    weatherDetail.cityName = weatherResponse.name
                    weatherDetail.countryName = weatherResponse.country
                    weatherDetail.temp = weatherResponse.current?.temp?.toInt()
                    weatherDetail.desc = weatherResponse.current?.weather?.first()?.description
                    _weatherLiveData.postValue(
                        weatherDetail
                    )
                }
            }catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _weatherDetailListLiveData.postValue(Event(State.error(e.message ?: "")))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _weatherDetailListLiveData.postValue(Event(State.error(e.message ?: "")))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _weatherDetailListLiveData.postValue(
                        Event(
                            State.error(
                                e.message ?: ""
                            )
                        )
                    )
                }
            }



        }
    }

    /**
     * add weather details to local DB
     *
     * @param WeatherDataResponse
     */
    private suspend fun addWeatherDetailtoDb(
        weatherResponse: WeatherDataResponse
    ) {
        val weatherDetail = WeatherDetail()
        weatherDetail.id = weatherResponse.id
        weatherDetail.icon = weatherResponse.current?.weather?.first()?.icon
        weatherDetail.cityName = weatherResponse.name
        weatherDetail.countryName = weatherResponse.country
        weatherDetail.temp = weatherResponse.current?.temp?.toInt()
        weatherDetail.dateTime = AppUtils.getCurrentDateTime("E, d MMM yyyy HH:mm:ss")
        weatherDetail.desc = weatherResponse.current?.weather?.first()?.description
        weatherRepo.addWeather(weatherDetail)
        fetchAllWeatherDetailsFromDb()
    }

    /**
     * fetch single weather record from DB by name
     *
     * @param cityName
     */
    fun fetchWeatherDetailFromDb(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDetail = weatherRepo.fetchWeatherDetail(cityName.toLowerCase())
            withContext(Dispatchers.Main) {
                if (weatherDetail != null) {
                    // Return true of current date and time is greater then the saved date and time of weather searched
                    if (AppUtils.isTimeExpired(weatherDetail.dateTime)) {
                        findCityWeather(cityName)
                    } else {
                        _weatherLiveData.postValue(
                                    weatherDetail
                        )
                    }

                } else {
                    findCityWeather(cityName)
                }

            }
        }
    }

    /**
     * set the default weather data
     *
     * @param WeatherDetail
     */
    fun selectWeatherData(weatherDetail: WeatherDetail) {
        _weatherLiveData.postValue(weatherDetail)
    }

    /**
     * fetch the latest record from DB
     */
    fun fetchLatestWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDetail = weatherRepo.fetchLatestWeatherDetail()
            withContext(Dispatchers.Main) {
                _weatherLiveData.postValue(weatherDetail)
            }

        }
    }

    /**
     * delete record from DB by ID
     *
     * @param CityId
     */
    fun deleteCity(cityId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepo.deleteCity(cityId)
        }
        fetchAllWeatherDetailsFromDb()
    }

    /**
     * fetch all weather data from DB
     */
    fun fetchAllWeatherDetailsFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDetailList = weatherRepo.fetchAllWeatherDetails()

            withContext(Dispatchers.Main) {
                _weatherDetailListLiveData.postValue(
                    Event(
                        State.success(weatherDetailList as MutableList<WeatherDetail>)
                    )
                )
            }
        }
    }

}
