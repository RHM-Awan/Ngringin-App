package com.rahmawan.ngringinapk.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.rahmawan.ngringinapk.PencapaianUtils
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.databinding.ActivityDetailUserBinding
import com.rahmawan.ngringinapk.firebase.user.EditUser
import com.rahmawan.ngringinapk.firebase.pencapaian.GetPencapaians
import com.rahmawan.ngringinapk.firebase.user.GetUser
import com.rahmawan.ngringinapk.model.Pencapaian
import com.rahmawan.ngringinapk.model.User
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailUserActivity : AppCompatActivity() {
    private lateinit var bind: ActivityDetailUserBinding
    private lateinit var uid:String
    val getUser = GetUser
    val getPencapaians = GetPencapaians
    lateinit var user:User
    private val editUser = EditUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(bind.root)
        title="Profil"
        initListener()
        uid = intent.extras?.getString("uid").toString()
        getUser.getUser(uid)
        getPencapaians.getAlQuran(uid)
        getPencapaians.getIqro(uid)
    }
    private fun initListener(){
        getPencapaians.setOnPencapaianListener(object : GetPencapaians.GetPencapaianListener{
            override fun onSuccess(result: ArrayList<Pencapaian>, type: String) {
                if(type == "alquran"){
                    bind.tvAlquran.text = "Al-Quran\n${PencapaianUtils.percentageAlquran(result)}%"
                }else{
                    bind.tvIqro.text = "Iqro\n${PencapaianUtils.percentageIqro(result)}%"
                }
            }

            override fun onSuccess(result: ArrayList<Pencapaian>, type: String, gender: String) {}

            override fun onCancelled(message: String) {
                Toast.makeText(this@DetailUserActivity,"Terjadi Kesalahan, Coba Lagi", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        getUser.setOnUserListener(object: GetUser.GetUserListener{
            override fun onSuccess(result: User?) {
                result?.let {
                    user=it
                    if(user.typeAccount=="admin"){
                        finish()
                    }
                    initData(it)
                }?:run{
                    Toast.makeText(this@DetailUserActivity,"Terjadi Kesalahan, Coba Lagi", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@DetailUserActivity,error, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        editUser.setOnEditUserListener(object : EditUser.EditUserListener{
            override fun onSuccess(result: String) {
                Toast.makeText(this@DetailUserActivity,result,Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@DetailUserActivity,error,Toast.LENGTH_SHORT).show()
            }
        })

        bind.btnEditAktif.setOnClickListener{
            if(user.statusActive=="aktif"){
                user.statusActive = "nonaktif"
            }else{
                user.statusActive = "aktif"
            }
            editUser.editUser(user)
        }

        bind.btnEditAdmin.setOnClickListener{
            user.typeAccount="admin"
            editUser.editUser(user)
        }
    }
    private fun initData(user: User){
        bind.tvNama.text=user.name
        bind.tvEmail.text=user.email
        bind.tvTelepon.text=user.phone
        bind.tvJk.text=user.gender
        bind.tvTglLahir.text= SimpleDateFormat("dd MMMM yyyy").format(Date(user.birthday!!))
        bind.tvAlamat.text=user.address
        if(user.image.equals("-")){
            Glide.with(this)
                .load(R.drawable.nophoto)
                .into(bind.ivProfil)
        }else{
            Glide.with(this)
                .load(user.image)
                .into(bind.ivProfil)
        }
        if(user.statusActive=="aktif"){
            bind.btnEditAktif.text = "Edit Tidak Aktif"
        }else{
            bind.btnEditAktif.text = "Edit Aktif"
        }
    }
}