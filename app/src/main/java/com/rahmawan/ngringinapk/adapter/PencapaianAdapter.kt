package com.rahmawan.ngringinapk.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rahmawan.ngringinapk.activity.EditPencapaianActivity
import com.rahmawan.ngringinapk.databinding.RowPencapaianBinding
import com.rahmawan.ngringinapk.model.Pencapaian

class PencapaianAdapter (private var list: List<Pencapaian>, private var type:String)
    : RecyclerView.Adapter<PencapaianAdapter.ViewHolder>() {

    inner class ViewHolder(val bind: RowPencapaianBinding)
        :RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val bind = RowPencapaianBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item:Pencapaian = list[position]
        with(viewHolder) {
            if(type.equals("alquran")){
                bind.tvNama.text = "Juz ${item.no}"
                bind.tvHalaman.text = "${item.hal}/20 Halaman"
            }else{
                bind.tvNama.text = "Iqro ${item.no}"
                if(position==0){
                    bind.tvHalaman.text = "${item.hal}/36 Halaman"
                }else{
                    bind.tvHalaman.text = "${item.hal}/32 Halaman"
                }
            }
            itemView.setOnClickListener{
                itemView.context.startActivity(
                    Intent(itemView.context,EditPencapaianActivity::class.java)
                    .putExtra("pencapaian",item)
                    .putExtra("type",type))
            }
        }
    }
    override fun getItemCount() = list.size
}