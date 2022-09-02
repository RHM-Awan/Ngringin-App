package com.rahmawan.ngringinapk.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahmawan.ngringinapk.activity.admin.DetailAgendaAdminActivity
import com.rahmawan.ngringinapk.activity.user.DetailAgendaUserActivity
import com.rahmawan.ngringinapk.databinding.RowAgendaBinding
import com.rahmawan.ngringinapk.model.Agenda
import java.text.SimpleDateFormat
import java.util.*

class AgendaAdapter(private val list: List<Agenda>, private val role:String)
    : RecyclerView.Adapter<AgendaAdapter.ViewHolder>() {

    inner class ViewHolder(val bind: RowAgendaBinding)
        : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val bind = RowAgendaBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: Agenda = list[position]
        with(viewHolder) {
            bind.tvJudul.text = item.title
            bind.tvTanggal.text = SimpleDateFormat("dd MMMM yyyy").format(Date(item.dateStart!!))
            itemView.setOnClickListener{
                if(role=="admin") {
                    itemView.context.startActivity(
                        Intent(itemView.context, DetailAgendaAdminActivity::class.java)
                            .putExtra("id", item.idAgenda)
                    )
                }else{
                    itemView.context.startActivity(
                        Intent(itemView.context, DetailAgendaUserActivity::class.java)
                            .putExtra("id", item.idAgenda)
                    )
                }
            }
        }
    }

    override fun getItemCount() = list.size
}