package com.rahmawan.ngringinapk.adapter

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.activity.admin.DetailKegiatanAdminActivity
import com.rahmawan.ngringinapk.activity.user.DetailKegiatanUserActivity
import com.rahmawan.ngringinapk.databinding.RowPresensiBinding
import com.rahmawan.ngringinapk.databinding.RowPresensiUserBinding
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Agenda
import com.rahmawan.ngringinapk.model.Presensi
import java.util.*
import java.util.concurrent.TimeUnit

class ListPresensiAdapter(private val list: List<Presensi>, private val agendaTimeStart:Long, private val id:String)
    : RecyclerView.Adapter<ListPresensiAdapter.ViewHolder>() {
    inner class ViewHolder(val bind: RowPresensiUserBinding)
        : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val bind = RowPresensiUserBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: Presensi = list[position]

        val dialogClickListener =
            DialogInterface.OnClickListener{ dialog, which ->
                when(which){
                    DialogInterface.BUTTON_POSITIVE -> {Firebase.presensiUserRef(item.id,id).removeValue()}
                    DialogInterface.BUTTON_NEGATIVE -> {}
                }
            }

        with(viewHolder) {
            bind.tvNama.text = item.user
            if(item.time==0L){
                bind.tvKeterangan.text = "Izin"
            }else{
                if(Date(item.time).after(Date(agendaTimeStart))) {
                    bind.tvKeterangan.text = "Terlambat"
                }else{
                    bind.tvKeterangan.text = "Tepat Waktu"
                }
            }
            val  builder: AlertDialog.Builder = AlertDialog.Builder(itemView.context)
            // klik hpus apsen
            itemView.setOnLongClickListener(object : View.OnLongClickListener{
                override fun onLongClick(p0: View?): Boolean {
                    builder.setMessage("Yakin ingin menghapus ${item.user}?").setPositiveButton(R.string.hapus, dialogClickListener)
                        .setNegativeButton(R.string.batal, dialogClickListener).show()
                    return true
                }
            })
        }
    }

    override fun getItemCount() = list.size
}