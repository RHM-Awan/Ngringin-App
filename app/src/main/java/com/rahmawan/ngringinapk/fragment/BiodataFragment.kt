package com.rahmawan.ngringinapk.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.activity.EditBiodataActivity
import com.rahmawan.ngringinapk.activity.ProfilActivity
import com.rahmawan.ngringinapk.databinding.FragmentBiodataBinding
import com.rahmawan.ngringinapk.model.User
import com.rahmawan.ngringinapk.firebase.user.GetUser
import java.text.SimpleDateFormat
import java.util.*

class BiodataFragment : Fragment() {
    private lateinit var bind:FragmentBiodataBinding
    private lateinit var act:ProfilActivity
    val getUser = GetUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentBiodataBinding.inflate(inflater,container,false)
        act=activity as ProfilActivity
        initListener()
        getUser.getUser(act.uid)
        return bind.root
    }

    private fun initListener(){
        getUser.setOnUserListener(object: GetUser.GetUserListener{
            override fun onSuccess(result: User?) {
                result?.let {
                    initData(it)
                }?:run{
                    Toast.makeText(requireContext(),"Terjadi Kesalahan, Coba Lagi",Toast.LENGTH_SHORT).show()
                    act.finish()
                }
            }
            override fun onFailure(error: String) {
                Toast.makeText(requireContext(),error,Toast.LENGTH_SHORT).show()
                act.finish()
            }
        })
        bind.btnEdit.setOnClickListener{
            startActivity(Intent(requireContext(),EditBiodataActivity::class.java))
        }
    }

    private fun initData(user:User){
        bind.tvNama.text=user.name
        bind.tvEmail.text=user.email
        bind.tvJk.text=user.gender
        bind.tvTelepon.text=user.phone
        bind.tvTglLahir.text= SimpleDateFormat("dd MMMM yyyy").format(Date(user.birthday!!))
        bind.tvAlamat.text=user.address
        Glide.with(this)
            .load(if(user.image.equals("-")) R.drawable.nophoto else user.image)
            .into(bind.ivProfil)
    }
}