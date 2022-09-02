package com.rahmawan.ngringinapk.fragment.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahmawan.ngringinapk.RecyclerUtils
import com.rahmawan.ngringinapk.adapter.PresensiAdapter
import com.rahmawan.ngringinapk.databinding.FragmentPresensiAdminBinding
import com.rahmawan.ngringinapk.firebase.agenda.GetAgendas
import com.rahmawan.ngringinapk.model.Agenda

class PresensiAdminFragment : Fragment() {
    private lateinit var bind: FragmentPresensiAdminBinding
    val getAgenda = GetAgendas
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentPresensiAdminBinding.inflate(inflater,container,false)
        initListener()
        bind.rvPresensi.addItemDecoration(RecyclerUtils.itemDecor(requireContext()))
        bind.rvPresensi.layoutManager = LinearLayoutManager(requireContext())
        getAgenda.getAgendas()
        return bind.root
    }
    private fun initListener(){
        getAgenda.setOnAgendasListener(object : GetAgendas.GetAgendasListener{
            override fun onSuccess(result: ArrayList<Agenda>) {
                val a = result.sortByDescending {
                    it.dateStart
                }
                bind.rvPresensi.adapter = PresensiAdapter(result, "admin")
                return a
            }
            override fun onCancelled(message: String) {
                Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}