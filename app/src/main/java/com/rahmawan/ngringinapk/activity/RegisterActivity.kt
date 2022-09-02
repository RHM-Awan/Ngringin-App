package com.rahmawan.ngringinapk.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.databinding.ActivityRegisterBinding
import com.rahmawan.ngringinapk.firebase.user.SaveUser
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity(){
    private lateinit var bind: ActivityRegisterBinding
    private var calendar: Calendar = Calendar.getInstance()
    val saveUser = SaveUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.hide()
        initListener()
        Glide.with(this)
            .load(R.drawable.logo)
            .into(bind.ivLogo)
    }
    private fun initListener() {
        saveUser.setOnSaveUserListener(object : SaveUser.SaveUserListener{
            override fun onSuccess(result: String) {
                Toast.makeText(this@RegisterActivity,result,Toast.LENGTH_SHORT).show()
                clearField()
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@RegisterActivity,error,Toast.LENGTH_SHORT).show()
            }
        })
        bind.btnDaftar.setOnClickListener{
            if(validate()){
                saveUser.saveUser(
                    bind.etNama.text.toString(),
                    bind.etEmail.text.toString().trim(),
                    bind.etPassword.text.toString().trim(),
                    stringDateToLong(bind.etTglLahir.text.toString()),
                    bind.etTelepon.text.toString().trim(),
                    bind.spJk.selectedItem.toString(),
                    bind.etAlamat.text.toString()
                )
            }
        }
        bind.tvLogin.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }
        bind.etTglLahir.setOnClickListener{
            showCalendar()
        }
    }
    private fun clearField() {
        bind.etNama.text = null
        bind.etEmail.text = null
        bind.etPassword.text = null
        bind.etTglLahir.text = null
        bind.etTelepon.text = null
        bind.etAlamat.text = null
        bind.spJk.setSelection(0)
    }
    private fun validate():Boolean{
        var valid = true
        if(bind.etNama.text.isEmpty()){
            valid=false
            bind.etNama.error="Nama Harus Diisi"
            bind.etNama.requestFocus()
        }
        if(bind.etEmail.text.isEmpty()){
            valid=false
            bind.etEmail.error="Email Harus Diisi"
            bind.etEmail.requestFocus()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(bind.etEmail.text.toString()).matches()){
            valid=false
            bind.etEmail.error="Email tidak valid"
            bind.etEmail.requestFocus()
        }
        if(bind.etPassword.text.isEmpty() || bind.etPassword.text.length < 6){
            valid=false
            bind.etPassword.error="Password Harus lebih dari 6 karakter"
            bind.etPassword.requestFocus()
        }
        if(bind.etTglLahir.text.isEmpty()){
            valid=false
            bind.etTglLahir.error="Tanggal Lahir Harus Diisi"
            bind.etTglLahir.requestFocus()
        }
        if(bind.etTelepon.text.isEmpty()){
            valid=false
            bind.etTelepon.error="Telepon Harus Diisi"
            bind.etTelepon.requestFocus()
        }
        if(bind.etAlamat.text.isEmpty()){
            valid=false
            bind.etAlamat.error="Alamat Harus Diisi"
            bind.etAlamat.requestFocus()
        }
        return valid
    }

    @SuppressLint("SimpleDateFormat")
    private fun showCalendar() {
        val datePickerDialog = DatePickerDialog(
            this,
            { view1: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                bind.etTglLahir.setText(SimpleDateFormat("dd MMMM yyyy").format(GregorianCalendar(year,month,dayOfMonth).time))
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        if (!datePickerDialog.isShowing) {
            datePickerDialog.show()
        }
    }
    @SuppressLint("SimpleDateFormat")
    fun stringDateToLong(stringDate:String):Long{
        return SimpleDateFormat("dd MMMM yyyy").parse(stringDate).time
    }
}