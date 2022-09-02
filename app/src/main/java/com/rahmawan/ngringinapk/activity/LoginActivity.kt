package com.rahmawan.ngringinapk.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.activity.admin.MainAdminActivity
import com.rahmawan.ngringinapk.activity.user.MainUserActivity
import com.rahmawan.ngringinapk.databinding.ActivityLoginBinding
import com.rahmawan.ngringinapk.firebase.user.GetUser
import com.rahmawan.ngringinapk.model.User
import com.rahmawan.ngringinapk.firebase.user.GetUsers

class LoginActivity : AppCompatActivity(){

    private lateinit var bind: ActivityLoginBinding
    private lateinit var prefs:SharedPreferences
    private var users:ArrayList<User> = ArrayList()
    val getUsers = GetUsers
    val getUser = GetUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.hide()
        initListener()
        prefs= getSharedPreferences("user", MODE_PRIVATE)
        val uid = prefs.getString("uid", "-")
        if(!uid.equals("-")){
            getUser.getUser(uid!!)
        }
        Glide.with(this)
            .load(R.drawable.logo)
            .into(bind.ivLogo)
        getUsers.getUsers()
    }

    private fun initListener(){
        getUser.setOnUserListener(object: GetUser.GetUserListener{
            override fun onSuccess(result: User?) {
                result?.let {
                    if(it.statusActive=="aktif") {
                        if (it.typeAccount.equals("user")) {
                            loginUser()
                        } else {
                            loginAdmin()
                        }
                    }else{
                        Toast.makeText(this@LoginActivity,"Akun Tidak Aktif",Toast.LENGTH_SHORT).show()
                    }
                }?:run{
                    Toast.makeText(this@LoginActivity,"Terjadi Kesalahan, Coba Lagi",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@LoginActivity,error,Toast.LENGTH_SHORT).show()
            }
        })
        getUsers.setOnUsersListener(object: GetUsers.GetUsersListener{
            override fun onSuccess(result: ArrayList<User>) {
                users=result
            }

            override fun onSuccess(aktif: ArrayList<User>, tidakAktif: ArrayList<User>) {}

            override fun onCancelled(message: String) {
                Toast.makeText(this@LoginActivity,"Terjadi Kesalahan, Coba lagi",Toast.LENGTH_SHORT).show()
            }
        })
        bind.tvRegister.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }
        bind.btnLogin.setOnClickListener{
            login(bind.etEmail.text.toString(),bind.etPassword.text.toString())
        }
    }
    private fun login(email:String,password:String){
        for(user in users){
            if(user.email.equals(email)&&user.password.equals(password)){
                if(user.statusActive=="aktif") {
                    with(prefs.edit()) {
                        putString("uid", user.idUser)
                        apply()
                    }
                    if (user.typeAccount.equals("user")) {
                        loginUser()
                    } else {
                        loginAdmin()
                    }
                }else{
                    Toast.makeText(this,"Akun Tidak Aktif",Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
        Toast.makeText(this,"Email atau password salah",Toast.LENGTH_SHORT).show()
    }
    fun loginUser(){
        startActivity(Intent(this,MainUserActivity::class.java))
        finish()
    }
    fun loginAdmin(){
        startActivity(Intent(this, MainAdminActivity::class.java))
        finish()
    }
}

