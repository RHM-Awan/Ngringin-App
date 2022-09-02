package com.rahmawan.ngringinapk.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.rahmawan.ngringinapk.databinding.ActivityEditPencapaianBinding
import com.rahmawan.ngringinapk.firebase.pencapaian.EditPencapaian
import com.rahmawan.ngringinapk.model.Pencapaian

class EditPencapaianActivity : AppCompatActivity() {
    private lateinit var bind: ActivityEditPencapaianBinding
    private lateinit var pencapaian:Pencapaian
    private lateinit var type:String
    private lateinit var uid:String
    private val editPencapaian = EditPencapaian
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityEditPencapaianBinding.inflate(layoutInflater)
        setContentView(bind.root)
        title="Edit Pencapaian"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        pencapaian= intent.extras?.get("pencapaian") as Pencapaian
        type = intent.extras?.getString("type").toString()
        bind.etHalaman.setText(pencapaian.hal.toString())
        if(type=="alquran"){
            bind.tvNama.text="Juz ${pencapaian.no}"
        }else{
            bind.tvNama.text="Iqro ${pencapaian.no}"
        }
        val prefs= getSharedPreferences("user", MODE_PRIVATE)
        uid = prefs.getString("uid", "-")!!
        initListener()
    }
    private fun initListener(){
        editPencapaian.setOnEditPencapaianListener(object: EditPencapaian.EditPencapaianListener{
            override fun onSuccess(result: String) {
                Toast.makeText(this@EditPencapaianActivity,result,Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@EditPencapaianActivity,error,Toast.LENGTH_SHORT).show()
            }
        })
        bind.btnSimpan.setOnClickListener{
            if(validate()){
                pencapaian.hal = bind.etHalaman.text.toString().toInt()
                if(type=="alquran"){
                    editPencapaian.editAlquran(uid,pencapaian)
                }else{
                    editPencapaian.editIqro(uid,pencapaian)
                }
            }
        }
    }
    private fun validate():Boolean {
        var valid = true
        if (bind.etHalaman.text.isEmpty()) {
            valid = false
            bind.etHalaman.error = "Halaman Harus Diisi"
        }else{
            val hal = bind.etHalaman.text.toString().toInt()
            if(hal<0){
                valid = false
                bind.etHalaman.error = "Halaman minimal 0"
            }else{
                if(type=="iqro"){
                    if(pencapaian.no==1&&hal>36){
                        valid = false
                        bind.etHalaman.error = "Halaman maksimal 36"
                    }else if(pencapaian.no!!>1&&hal>32){
                        valid = false
                        bind.etHalaman.error = "Halaman maksimal 32"
                    }
                }else if(hal>20){
                    valid = false
                    bind.etHalaman.error = "Halaman maksimal 20"
                }
            }
        }
        return valid
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}