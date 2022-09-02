package com.rahmawan.ngringinapk.activity.admin

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.activity.MapActivity
import com.rahmawan.ngringinapk.databinding.ActivityDetailAgendaAdminBinding
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.firebase.agenda.GetAgenda
import com.rahmawan.ngringinapk.model.Agenda
import java.text.SimpleDateFormat
import java.util.*

class DetailAgendaAdminActivity : AppCompatActivity() {
    private lateinit var bind: ActivityDetailAgendaAdminBinding
    private lateinit var latLng: LatLng
    private lateinit var getAgenda:GetAgenda
    private lateinit var id:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailAgendaAdminBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        id=intent.extras?.getString("id").toString()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        getAgenda=GetAgenda
        getAgenda.setOnAgendaListener(object: GetAgenda.GetAgendaListener{
            override fun onSuccess(result: Agenda?) {
                result?.let {
                    initData(it)
                    latLng= LatLng(it.latitude!!,it.longtitude!!)
                }?:run{
                    Toast.makeText(this@DetailAgendaAdminActivity,"Data Telah Dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@DetailAgendaAdminActivity,error, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        getAgenda.getAgenda(id)
    }

    val dialogClickListener =
        DialogInterface.OnClickListener{ dialog, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {Firebase.agendaRef.child(id).removeValue()}
                DialogInterface.BUTTON_NEGATIVE -> {}
            }
        }

    private fun initListener(){
        val  builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(bind.btnHapus.context)
        bind.btnHapus.setOnClickListener(View.OnClickListener {
            builder.setMessage("Yakin ingin menghapus Agenda?").setPositiveButton(R.string.hapus, dialogClickListener)
                .setNeutralButton(R.string.batal, dialogClickListener).show()
            return@OnClickListener
        })

        bind.btnLokasi.setOnClickListener{
            startActivity(Intent(this,MapActivity::class.java).putExtra("latLng",latLng))
        }
        bind.btnEdit.setOnClickListener{
            startActivity(Intent(this,EditAgendaActivity::class.java).putExtra("id",id))
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