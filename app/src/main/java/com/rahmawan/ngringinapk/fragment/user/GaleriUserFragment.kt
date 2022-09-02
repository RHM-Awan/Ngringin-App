package com.rahmawan.ngringinapk.fragment.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahmawan.ngringinapk.RecyclerUtils
import com.rahmawan.ngringinapk.activity.admin.CreateGaleriActivity
import com.rahmawan.ngringinapk.adapter.GaleriAdapter
import com.rahmawan.ngringinapk.databinding.FragmentGaleriUserBinding
import com.rahmawan.ngringinapk.firebase.galeri.GetGaleris
import com.rahmawan.ngringinapk.model.Galeri

class GaleriUserFragment : Fragment() {
    private lateinit var bind: FragmentGaleriUserBinding
    private val getGaleris= GetGaleris
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentGaleriUserBinding.inflate(inflater,container,false)
        bind.rvGaleri.addItemDecoration(RecyclerUtils.itemDecor(requireContext()))
        bind.rvGaleri.layoutManager = LinearLayoutManager(requireContext())
        initListener()
        getGaleris.getGaleris()
        return bind.root
    }
    private fun initListener(){
        getGaleris.setOnGalerisListener(object :GetGaleris.GetGalerisListener{
            override fun onSuccess(result: ArrayList<Galeri>) {
                val a = result.sortByDescending {
                    it.tanggal
                }
                bind.rvGaleri.adapter = GaleriAdapter(result, "user")
                return a
            }
            override fun onCancelled(message: String) {
                Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}