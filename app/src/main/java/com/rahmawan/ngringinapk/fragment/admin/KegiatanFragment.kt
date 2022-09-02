package com.rahmawan.ngringinapk.fragment.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahmawan.ngringinapk.RecyclerUtils
import com.rahmawan.ngringinapk.databinding.FragmentKegiatanBinding
import com.rahmawan.ngringinapk.databinding.FragmentPresensiUserBinding
import com.rahmawan.ngringinapk.firebase.agenda.GetAgenda
import com.rahmawan.ngringinapk.firebase.agenda.GetAgendas
import com.rahmawan.ngringinapk.model.Agenda
import java.text.SimpleDateFormat
import java.util.*

class KegiatanFragment(val agenda: Agenda) : Fragment() {
    private lateinit var bind: FragmentKegiatanBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentKegiatanBinding.inflate(inflater,container,false)
        bind.tvJudul.text=agenda.title
        bind.tvKeterangan.text=agenda.body
        bind.tvLokasi.text=agenda.address
        agenda.dateStart?.let {
            bind.tvMulai.text= SimpleDateFormat("HH:mm dd MMM yyyy").format(Date(it))
        }
        agenda.dateFinish?.let {
            bind.tvSelesai.text= SimpleDateFormat("HH:mm dd MMM yyyy").format(Date(it))
        }
        return bind.root
    }
}