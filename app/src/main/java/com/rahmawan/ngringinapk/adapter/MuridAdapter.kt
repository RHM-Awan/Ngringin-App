package com.rahmawan.ngringinapk.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.activity.admin.DetailUserActivity
import com.rahmawan.ngringinapk.databinding.RowUserBinding
import com.rahmawan.ngringinapk.model.User
import java.util.*

class MuridAdapter (private val list: List<User>)
    : RecyclerView.Adapter<MuridAdapter.ViewHolder>() {
    var currentTime = Calendar.getInstance().time
    inner class ViewHolder(val bind: RowUserBinding)
        : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val bind = RowUserBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: User = list[position]
        with(viewHolder) {
            Glide.with(itemView.context)
                .load(if(item.image.equals("-")) R.drawable.nophoto else item.image)
                .into(bind.ivProfil)
            bind.tvNama.text = item.name
            bind.tvUmur.text = "Umur ${currentTime.year-Date(item.birthday!!).year} tahun"
            itemView.setOnClickListener{
                itemView.context.startActivity(
                    Intent(itemView.context, DetailUserActivity::class.java)
                        .putExtra("uid",item.idUser))
            }
        }
    }

    override fun getItemCount() = list.size
}