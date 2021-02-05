package com.prederikochan.weatherapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prederikochan.weatherapp.R
import com.prederikochan.weatherapp.data.model.Daily
import com.prederikochan.weatherapp.utils.getDays
import kotlinx.android.synthetic.main.item_weather.view.*

class HomeListAdapter(val context: Context) : RecyclerView.Adapter<HomeListAdapter.ViewHolder>() {

    private var datas = ArrayList<Daily>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_weather, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (datas.isNotEmpty()) {
            val model = datas[position]
            holder.setModel(model, position)
        }
    }

    override fun getItemCount(): Int {
        return 6
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setModel(model: Daily, position: Int) {
            itemView.itemTxtDay.text = if (position == 0)
                "Today"
            else
                getDays(model.dt!!)

            itemView.itemTxtTemp.text = "${model.temp?.max?.toInt()}°C/${model.temp?.min?.toInt()}°C"
            Glide.with(itemView)
                .load("http://openweathermap.org/img/wn/${model.weather?.get(0)?.icon}@2x.png")
                .into(itemView.itemIcWeather)
        }
    }

    fun updateData(data: List<Daily>) {
        datas.clear()
        datas.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        datas.clear()
        notifyDataSetChanged()
    }
}