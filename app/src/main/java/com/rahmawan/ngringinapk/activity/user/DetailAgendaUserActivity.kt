package com.rahmawan.ngringinapk.activity.user

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.rahmawan.ngringinapk.activity.MapActivity
import com.rahmawan.ngringinapk.databinding.ActivityDetailAgendaUserBinding
import com.rahmawan.ngringinapk.firebase.agenda.GetAgenda
import com.rahmawan.ngringinapk.firebase.user.GetUser
import com.rahmawan.ngringinapk.model.Agenda
import com.rahmawan.ngringinapk.model.User
import java.text.SimpleDateFormat
import java.util.*

class DetailAgendaUserActivity : AppCompatActivity() {
    private val getAgenda=GetAgenda
    private lateinit var id:String
    private lateinit var bind: ActivityDetailAgendaUserBinding
    private lateinit var latLng: LatLng
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailAgendaUserBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        id=intent.extras?.getString("id").toString()
        initListener()
        getAgenda.getAgenda(id)
    }
    private fun initListener(){
        getAgenda.setOnAgendaListener(object: GetAgenda.GetAgendaListener{
            override fun onSuccess(result: Agenda?) {
                result?.let {
                    initData(it)
                    latLng= LatLng(it.latitude!!,it.longtitude!!)
                }?:run{
                    Toast.makeText(this@DetailAgendaUserActivity,"Data Telah Dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@DetailAgendaUserActivity,error, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        bind.btnLokasi.setOnClickListener{
            startActivity(Intent(this, MapActivity::class.java).putExtra("latLng",latLng))
        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun initData(agenda: Agenda){
        title=agenda.title
        bind.tvJudul.text=agenda.title
        bind.tvKeterangan.text=agenda.body
        bind.tvLokasi.text=agenda.address
        bind.tvMulai.text= SimpleDateFormat("HH:mm dd MMM yyyy").format(Date(agenda.dateStart!!))
        bind.tvSelesai.text= SimpleDateFormat("HH:mm dd MMM yyyy").format(Date(agenda.dateFinish!!))
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}