package com.rahmawan.ngringinapk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rahmawan.ngringinapk.databinding.RowSliderGaleriBinding

class SliderAdapter (private val list: List<String>)
    : RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    inner class ViewHolder(val bind: RowSliderGaleriBinding)
        : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val bind = RowSliderGaleriBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            Glide.with(itemView.context)
                .load(list[position])
                .into(bind.ivGambar)
        }
    }

    override fun getItemCount() = list.size
}