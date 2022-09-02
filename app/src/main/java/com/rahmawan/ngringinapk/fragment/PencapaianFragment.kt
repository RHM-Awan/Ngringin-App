package com.rahmawan.ngringinapk.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahmawan.ngringinapk.activity.ProfilActivity
import com.rahmawan.ngringinapk.adapter.PencapaianAdapter
import com.rahmawan.ngringinapk.databinding.FragmentPencapaianBinding
import com.rahmawan.ngringinapk.firebase.pencapaian.GetPencapaians
import com.rahmawan.ngringinapk.model.Pencapaian

class PencapaianFragment : Fragment() {
    private lateinit var bind: FragmentPencapaianBinding
    private lateinit var act: ProfilActivity
    val getPencapaians = GetPencapaians
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentPencapaianBinding.inflate(inflater,container,false)
        act=activity as ProfilActivity
        initListener()
        getPencapaians.getAlQuran(act.uid)
        getPencapaians.getIqro(act.uid)
        bind.rvAlquran.layoutManager = LinearLayoutManager(requireContext())
        bind.rvIqro.layoutManager = LinearLayoutManager(requireContext())
        return bind.root
    }
    private fun initListener(){
        getPencapaians.setOnPencapaianListener(object : GetPencapaians.GetPencapaianListener{
            override fun onSuccess(result: ArrayList<Pencapaian>, type: String) {
                if(type == "alquran"){
                    bind.rvAlquran.adapter = PencapaianAdapter(result,type)
                }else{
                    bind.rvIqro.adapter = PencapaianAdapter(result,type)
                }
            }

            override fun onSuccess(result: ArrayList<Pencapaian>, type: String, gender: String) {}

            override fun onCancelled(message: String) {
                Toast.makeText(requireContext(),"Terjadi Kesalahan, Coba Lagi", Toast.LENGTH_SHORT).show()
                act.finish()
            }
        })
    }
}