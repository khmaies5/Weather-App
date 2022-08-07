package com.khmaies.psaweathertest.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.khmaies.data.model.WeatherDetail
import com.khmaies.psaweathertest.R
import com.khmaies.psaweathertest.databinding.ListItemSearchedCityBinding
import com.khmaies.psaweathertest.ui.util.AppUtils

class SearchResultAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private val weatherDetailList = ArrayList<WeatherDetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListItemSearchedCityBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_searched_city,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(weatherDetailList[position])
    }

    override fun getItemCount(): Int = weatherDetailList.size

    fun setData(newWeatherDetail : List<WeatherDetail>) {
        weatherDetailList.clear()
        weatherDetailList.addAll(newWeatherDetail)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ListItemSearchedCityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(weatherDetail: WeatherDetail) {

            binding.apply {
                //TODO set icon image
                weatherDetail.icon?.let {
                    AppUtils.getWeatherIcon(
                        it
                    )
                }?.let { imageWeatherSymbol.setImageResource(it) }
                textCityName.text =
                    "${weatherDetail.cityName?.capitalize()}, ${weatherDetail.countryName}"
                textTemperature.text = weatherDetail.temp.toString()
                textDateTime.text = weatherDetail.dateTime
            }
            itemView.setOnClickListener {
                onClickListener.onClick(weatherDetail)
            }
        }
    }
    class OnClickListener(val clickListener: (weatherDetail: WeatherDetail) -> Unit) {
        fun onClick(weatherDetail: WeatherDetail) = clickListener(weatherDetail)
    }
}
