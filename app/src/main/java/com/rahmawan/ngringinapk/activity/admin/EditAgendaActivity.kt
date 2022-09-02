package com.rahmawan.ngringinapk.activity.admin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.databinding.ActivityEditAgendaBinding
import com.rahmawan.ngringinapk.databinding.DateTimePickerBinding
import com.rahmawan.ngringinapk.firebase.agenda.EditAgenda
import com.rahmawan.ngringinapk.firebase.agenda.GetAgenda
import com.rahmawan.ngringinapk.model.Agenda
import java.text.SimpleDateFormat
import java.util.*

class EditAgendaActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var bind: ActivityEditAgendaBinding
    private lateinit var id:String
    private val getAgenda= GetAgenda
    private val editAgenda= EditAgenda
    private lateinit var agenda: Agenda
    private lateinit var maps: GoogleMap
    private lateinit var dateTimePickerBinding: DateTimePickerBinding
    private lateinit var timeType:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityEditAgendaBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title="Edit Agenda"
        id=intent.extras?.getString("id").toString()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fc_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        dateTimePickerBinding = DateTimePickerBinding.bind(View.inflate(this, R.layout.date_time_picker, null))
        initListener()
        getAgenda.getAgenda(id)
    }
    private fun initListener(){
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setView(dateTimePickerBinding.root)
        editAgenda.setOnEditAgendaListener(object:EditAgenda.EditAgendaListener{
            override fun onSuccess(result: String) {
                Toast.makeText(this@EditAgendaActivity,"Data Telah Diedit", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@EditAgendaActivity,"Terjadi kesalahan, Coba Lagi", Toast.LENGTH_SHORT).show()
            }
        })
        getAgenda.setOnAgendaListener(object: GetAgenda.GetAgendaListener{
            override fun onSuccess(result: Agenda?) {
                result?.let {
                    initData(it)
                    agenda=it

                }?:run{
                    Toast.makeText(this@EditAgendaActivity,"Data Telah Dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@EditAgendaActivity,error, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        dateTimePickerBinding.dateTimeSet.setOnClickListener{
            val calendar: Calendar = GregorianCalendar(
                dateTimePickerBinding.datePicker.year,
                dateTimePickerBinding.datePicker.month,
                dateTimePickerBinding.datePicker.dayOfMonth,
                dateTimePickerBinding.timePicker.currentHour,
                dateTimePickerBinding.timePicker.currentMinute
            )
            if(timeType=="mulai") {
                bind.etMulai.setText(SimpleDateFormat("HH:mm dd MMM yyyy").format(calendar.time))
            }else{
                bind.etSelesai.setText(SimpleDateFormat("HH:mm dd MMM yyyy").format(calendar.time))
            }
            alertDialog.dismiss()
        }
        bind.etMulai.setOnClickListener{
            alertDialog.show()
            timeType = "mulai"
        }
        bind.etSelesai.setOnClickListener{
            alertDialog.show()
            timeType = "selesai"
        }
        bind.btnSimpan.setOnClickListener{
            if(validate()){
                agenda.title=bind.etJudul.text.toString()
                agenda.body=bind.etKeterangan.text.toString()
                agenda.dateStart=stringDateToLong(bind.etMulai.text.toString())
                agenda.dateFinish=stringDateToLong(bind.etSelesai.text.toString())
                agenda.address=bind.etLokasi.text.toString()
                editAgenda.editAgenda(agenda)
            }
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        maps = googleMap
        if(this@EditAgendaActivity::agenda.isInitialized){
            setMaps(LatLng(agenda.latitude!!,agenda.longtitude!!))
            maps.setOnMapClickListener(object: GoogleMap.OnMapClickListener{
                override fun onMapClick(p0: LatLng) {
                    agenda.latitude = p0.latitude
                    agenda.longtitude = p0.longitude
                    bind.tvCoordinat.text = "${agenda.latitude}, ${agenda.longtitude}"
                    setMaps(LatLng(agenda.latitude!!,agenda.longtitude!!))
                }
            })
        }
    }
    private fun setMaps(latLng:LatLng){
        maps.clear()
        maps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        maps.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Lokasi Disini")
        )
    }
    private fun validate():Boolean{
        var valid=true
        if(bind.etJudul.text.isEmpty()) {
            valid=false
            bind.etJudul.error="Judul Harus Diisi"
        }
        if(bind.etMulai.text.isEmpty()) {
            valid=false
            Toast.makeText(this,"Waktu Mulai Harus Diisi",Toast.LENGTH_SHORT).show()
        }
        if(bind.etSelesai.text.isEmpty()) {
            valid=false
            Toast.makeText(this,"Waktu Selesai Harus Diisi",Toast.LENGTH_SHORT).show()
        }else if(stringDateToDate(bind.etMulai.text.toString()).after(stringDateToDate(bind.etSelesai.text.toString()))){
            valid=false
            Toast.makeText(this,"Waktu Selesai Kurang",Toast.LENGTH_SHORT).show()
        }
        if(bind.etKeterangan.text.isEmpty()) {
            valid=false
            bind.etKeterangan.error="Keterangan Harus Diisi"
        }
        if(bind.etLokasi.text.isEmpty()) {
            valid=false
            bind.etLokasi.error="Lokasi Harus Diisi"
        }
        return valid
    }
    @SuppressLint("SimpleDateFormat")
    private fun initData(agenda: Agenda){

        bind.tvCoordinat.text = "${agenda.latitude}, ${agenda.longtitude}"
        bind.etJudul.setText(agenda.title)
        bind.etKeterangan.setText(agenda.body)
        bind.etLokasi.setText(agenda.address)
        bind.etMulai.setText(SimpleDateFormat("HH:mm dd MMM yyyy").format(Date(agenda.dateStart!!)))
        bind.etSelesai.setText(SimpleDateFormat("HH:mm dd MMM yyyy").format(Date(agenda.dateFinish!!)))
        if(this::maps.isInitialized){
            setMaps(LatLng(agenda.latitude!!,agenda.longtitude!!))
        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun stringDateToLong(stringDate:String):Long{
        return SimpleDateFormat("HH:mm dd MMM yyyy").parse(stringDate).time
    }
    private fun stringDateToDate(stringDate:String):Date{
        return SimpleDateFormat("HH:mm dd MMM yyyy").parse(stringDate)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}