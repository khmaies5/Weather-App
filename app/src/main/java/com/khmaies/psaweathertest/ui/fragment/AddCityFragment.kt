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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.khmaies.data.model.WeatherDetail
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
    var newWeatherDetails : MutableList<WeatherDetail> = mutableListOf()

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

        // on below line we are creating a method to create item touch helper
        // method for adding swipe to delete functionality.
        // in this we are specifying drag direction and position to right
        // on below line we are creating a method to create item touch helper
        // method for adding swipe to delete functionality.
        // in this we are specifying drag direction and position to right
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // this method is called when we swipe our item to right direction.
// on below line we are getting the item at a particular position.
                val deletedWeather: WeatherDetail =
                    newWeatherDetails[viewHolder.adapterPosition]

                // below line is to get the position
                // of the item at that position.
                val position = viewHolder.adapterPosition

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                newWeatherDetails.removeAt(viewHolder.adapterPosition)

                deletedWeather.id?.let { sharedViewModel.deleteCity(it) }

                // below line is to notify our item is removed from adapter.
                searchResultAdapter.notifyItemRemoved(viewHolder.adapterPosition)

                // below line is to display our snackbar with action.
                Snackbar.make(binding.recyclerViewSearchedCityTemperature, "Deleted ${deletedWeather.cityName}", Snackbar.LENGTH_LONG)
                    .show()
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(binding.recyclerViewSearchedCityTemperature)


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
                        newWeatherDetails = state.data
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