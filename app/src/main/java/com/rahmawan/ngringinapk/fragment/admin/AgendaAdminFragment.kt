package com.rahmawan.ngringinapk.fragment.admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahmawan.ngringinapk.RecyclerUtils
import com.rahmawan.ngringinapk.activity.admin.CreateAgendaActivity
import com.rahmawan.ngringinapk.adapter.AgendaAdapter
import com.rahmawan.ngringinapk.databinding.FragmentAgendaAdminBinding
import com.rahmawan.ngringinapk.firebase.agenda.GetAgendas
import com.rahmawan.ngringinapk.model.Agenda
import kotlin.collections.ArrayList

class AgendaAdminFragment : Fragment() {
    private lateinit var bind: FragmentAgendaAdminBinding
    val getAgenda = GetAgendas
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentAgendaAdminBinding.inflate(inflater,container,false)
        initListener()
        bind.rvAgenda.addItemDecoration(RecyclerUtils.itemDecor(requireContext()))
        bind.rvAgenda.layoutManager = LinearLayoutManager(requireContext())
        getAgenda.getAgendas()
        return bind.root
    }
    private fun initListener(){
        getAgenda.setOnAgendasListener(object : GetAgendas.GetAgendasListener{
            override fun onSuccess(result: ArrayList<Agenda>) {
                val a = result.sortByDescending {
                    it.dateStart
                }
                bind.rvAgenda.adapter = AgendaAdapter(result, "admin")
                return a
            }
            override fun onCancelled(message: String) {
                Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
            }
        })
        bind.fabAdd.setOnClickListener{
            startActivity(Intent(requireContext(),CreateAgendaActivity::class.java))
        }
    }
}