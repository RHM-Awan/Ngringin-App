package com.rahmawan.ngringinapk.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahmawan.ngringinapk.activity.admin.DetailAgendaAdminActivity
import com.rahmawan.ngringinapk.activity.admin.DetailKegiatanAdminActivity
import com.rahmawan.ngringinapk.activity.user.DetailKegiatanUserActivity
import com.rahmawan.ngringinapk.databinding.RowPresensiBinding
import com.rahmawan.ngringinapk.model.Agenda
import java.util.*
import java.util.concurrent.TimeUnit


class PresensiAdapter(private val list: List<Agenda>, private val role:String)
    : RecyclerView.Adapter<PresensiAdapter.ViewHolder>() {
    var currentTime = Calendar.getInstance().time
    inner class ViewHolder(val bind: RowPresensiBinding)
        : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val bind = RowPresensiBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: Agenda = list[position]
        with(viewHolder) {
            bind.tvJudul.text = item.title
            if(currentTime.after(Date(item.dateFinish!!))){
                bind.tvKeterangan.text = "Kegiatan telah selesai"
            }else if(currentTime.before(Date(item.dateStart!!))){
                bind.tvKeterangan.text = null
               // "${TimeUnit.MINUTES.convert(Date(item.dateStart!!).time - currentTime.time,TimeUnit.MILLISECONDS)} Menit  lagi dimulai"
                var b = TimeUnit.MINUTES.convert(Date(item.dateStart!!).time - currentTime.time,TimeUnit.MILLISECONDS)
                var jam3 = -1
                var menit = b % 60
                var hari = -1
                var jam2 = b % 1440
                var jam = -1

                if ( b < 60){
                    bind.tvKeterangan.text = "$menit menit lagi dimulai"
                }else if ( b > 60 && b < 1440 ){
                    for (bc in 0 .. jam2 step 60){
                        jam++
                    }
                    bind.tvKeterangan.text = "$jam jam $menit menit lagi dimulai"
                } else if(b > 1440){
                    for (value in 0 .. b step 60){
                            jam3++
                    }
                    for (ab in 0 .. jam3 step 24){
                        hari++
                    }
                    for (bc in 0 .. jam2 step 60){
                        jam++
                    }
                        bind.tvKeterangan.text = "$hari Hari $jam Jam $menit Menit lagi dimulai"

                }

            }else{
                bind.tvKeterangan.text = "Sedang Dimulai"
            }
            itemView.setOnClickListener{
                if(role=="admin") {
                    itemView.context.startActivity(
                        Intent(itemView.context, DetailKegiatanAdminActivity::class.java)
                            .putExtra("id", item.idAgenda)
                    )
                }else{
                    itemView.context.startActivity(
                        Intent(itemView.context, DetailKegiatanUserActivity::class.java)
                            .putExtra("id", item.idAgenda)
                    )
                }
            }
        }
    }

    override fun getItemCount() = list.size
}