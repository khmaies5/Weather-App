package com.khmaies.psaweathertest.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.khmaies.psaweathertest.databinding.FragmentAddCityBinding
import com.khmaies.psaweathertest.ui.adapter.SearchResultAdapter
import com.khmaies.psaweathertest.ui.common.WeatherApp
import com.khmaies.psaweathertest.ui.util.EventObserver
import com.khmaies.psaweathertest.ui.util.State
import com.khmaies.psaweathertest.ui.viewmodel.WeatherViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddCityFragment : Fragment() {

    private var _binding: FragmentAddCityBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var sharedViewModel: WeatherViewModel

    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val appContainer = (requireActivity().application as WeatherApp).appContainer
        val viewModelFactory = appContainer.viewModelFactory
        sharedViewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[WeatherViewModel::class.java]
        _binding = FragmentAddCityBinding.inflate(inflater, container, false)
        observeCall()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.fetchAllWeatherDetailsFromDb()
        setUI()

    }

    private fun setUI() {
        initRecyclerView()
        binding.inputFindCityWeather.setOnEditorActionListener { textView, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                sharedViewModel.fetchWeatherDetailFromDb((textView as EditText).text.toString())
                sharedViewModel.fetchAllWeatherDetailsFromDb()
            }
            false
        }
    }

    private fun initRecyclerView() {
        searchResultAdapter = SearchResultAdapter(SearchResultAdapter.OnClickListener{weatherDetail ->
            sharedViewModel.selectWeatherData(weatherDetail)
        })

        val mLayoutManager = LinearLayoutManager(
            this.context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewSearchedCityTemperature.apply {
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = searchResultAdapter
        }
    }

    private fun observeCall() {


        sharedViewModel.weatherDetailListLiveData.observe(
            viewLifecycleOwner,
            EventObserver { state ->
                when (state) {
                    is State.Loading -> {

                    }
                    is State.Success -> {
                        binding.inputFindCityWeather.text?.clear()
                        searchResultAdapter.setData(state.data)
                    }
                    is State.Error -> {
                        Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()

                    }
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}