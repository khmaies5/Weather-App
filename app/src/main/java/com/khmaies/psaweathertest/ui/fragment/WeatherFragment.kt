package com.khmaies.psaweathertest.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.khmaies.data.model.WeatherDetail
import com.khmaies.psaweathertest.R
import com.khmaies.psaweathertest.databinding.FragmentWeatherBinding
import com.khmaies.psaweathertest.ui.common.WeatherApp
import com.khmaies.psaweathertest.ui.util.AppUtils
import com.khmaies.psaweathertest.ui.util.EventObserver
import com.khmaies.psaweathertest.ui.util.State
import com.khmaies.psaweathertest.ui.viewmodel.WeatherViewModel
import java.time.Duration

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var sharedViewModel: WeatherViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val appContainer = (requireActivity().application as WeatherApp).appContainer
        val viewModelFactory = appContainer.viewModelFactory
        sharedViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[WeatherViewModel::class.java]
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)




        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeApiCall()
        sharedViewModel.fetchLatestWeather()


       // initUi()

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_WeatherFragment_to_AddCityFragment)

        }
    }

    fun observeApiCall(){
        sharedViewModel.weatherLiveData.observe(
            viewLifecycleOwner
        ) {
            initUi()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initUi() {
        if(sharedViewModel.weatherLiveData.value == null) {
            binding.textCityName.text = getString(R.string.empty_view)
            //observeApiCall()

        } else {
            binding.textLabelDegree.visibility = View.VISIBLE
            binding.textTodaysDate.visibility = View.VISIBLE
            binding.textLabelToday.visibility = View.VISIBLE
            sharedViewModel.weatherLiveData.value?.icon?.let {
                AppUtils.getWeatherIcon(
                    it
                )
            }?.let { binding.imageWeatherSymbol.setImageResource(it) }

            changeBgAccToTemp(sharedViewModel.weatherLiveData.value?.icon)
            binding.textTemperature.text = sharedViewModel.weatherLiveData.value?.temp.toString()
            binding.textTodaysDate.text = AppUtils.getCurrentDateTime("E, d MMM yyyy")
            binding.textCityName.text =
                "${sharedViewModel.weatherLiveData.value?.cityName?.capitalize()}, ${sharedViewModel.weatherLiveData.value?.countryName}"
            binding.textWeatherDesc.text = sharedViewModel.weatherLiveData.value?.desc
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeBgAccToTemp(iconCode: String?) {
        when (iconCode) {
            "04d", "04n", "03d", "03n" -> binding.imageCity.setImageResource(R.drawable.cloudybg)
            "02d", "02n" -> binding.imageCity.setImageResource(R.drawable.partlycloudybg)
            "09d", "09n","10d","10n","50n","50d" -> binding.imageCity.setImageResource(R.drawable.rainybg)
            "13d", "13n" -> binding.imageCity.setImageResource(R.drawable.snowybg)
            "11d", "11n" -> binding.imageCity.setImageResource(R.drawable.stormybg)
            "01d", "01n" -> binding.imageCity.setImageResource(R.drawable.sunnybg)
            else -> binding.imageCity.setImageResource(R.drawable.stormybg)
        }
    }
}