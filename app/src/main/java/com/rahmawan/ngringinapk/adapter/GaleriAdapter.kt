package com.rahmawan.ngringinapk.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.activity.admin.DetailGaleriAdminActivity
import com.rahmawan.ngringinapk.activity.user.DetailGaleriUserActivity
import com.rahmawan.ngringinapk.databinding.RowGaleriBinding
import com.rahmawan.ngringinapk.model.Galeri
import java.text.SimpleDateFormat
import java.util.*

class GaleriAdapter(private val list: List<Galeri>, private val role:String)
    : RecyclerView.Adapter<GaleriAdapter.ViewHolder>() {

    inner class ViewHolder(val bind: RowGaleriBinding)
        : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val bind = RowGaleriBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: Galeri = list[position]
        with(viewHolder) {
            bind.tvJudul.text = item.judul
            bind.tvTanggal.text = SimpleDateFormat("dd MMMM yyyy").format(Date(item.tanggal!!))
            if(item.gambar1!=null){
                Glide.with(itemView.context)
                    .load(item.gambar1)
                    .into(bind.ivFirst)
            }else if(item.gambar2!=null){
                Glide.with(itemView.context)
                    .load(item.gambar2)
                    .into(bind.ivFirst)
            }else if(item.gambar3!=null){
                Glide.with(itemView.context)
                    .load(item.gambar3)
                    .into(bind.ivFirst)
            }else{
                Glide.with(itemView.context)
                    .load(R.drawable.ic_baseline_add_24)
                    .into(bind.ivFirst)
            }
            itemView.setOnClickListener{
                if(role=="admin") {
                    itemView.context.startActivity(
                        Intent(itemView.context, DetailGaleriAdminActivity::class.java)
                            .putExtra("id", item.idGaleri)
                    )
                }else{
                    itemView.context.startActivity(
                        Intent(itemView.context, DetailGaleriUserActivity::class.java)
                            .putExtra("id", item.idGaleri)
                    )
                }
            }
        }
    }

    override fun getItemCount() = list.size
}