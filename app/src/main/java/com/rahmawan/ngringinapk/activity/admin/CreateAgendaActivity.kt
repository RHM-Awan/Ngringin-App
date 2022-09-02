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
import com.rahmawan.ngringinapk.databinding.ActivityCreateAgendaBinding
import com.rahmawan.ngringinapk.databinding.DateTimePickerBinding
import com.rahmawan.ngringinapk.firebase.agenda.SaveAgenda
import java.text.SimpleDateFormat
import java.util.*

class CreateAgendaActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var bind:ActivityCreateAgendaBinding
    private lateinit var maps: GoogleMap
    private lateinit var dateTimePickerBinding: DateTimePickerBinding
    private lateinit var timeType:String
    private var latLng = LatLng(-7.7604026, 110.404842)
    //-7.7604026, 110.404842
    // lokasi skring -7.7518963, 110.4115315
    val saveAgenda = SaveAgenda
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityCreateAgendaBinding.inflate(layoutInflater)
        setContentView(bind.root)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fc_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        dateTimePickerBinding = DateTimePickerBinding.bind(View.inflate(this, R.layout.date_time_picker, null))
        initListener()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Buat Agenda"
    }
    private fun initListener(){
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setView(dateTimePickerBinding.root)
        saveAgenda.setOnSaveAgendaListener(object : SaveAgenda.SaveAgendaListener{
                override fun onSuccess(result: String) {
                    Toast.makeText(this@CreateAgendaActivity,result,Toast.LENGTH_SHORT).show()
                    clearField()
                }
                override fun onFailure(error: String) {
                    Toast.makeText(this@CreateAgendaActivity,error,Toast.LENGTH_SHORT).show()
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
                saveAgenda.saveAgenda(
                    bind.etJudul.text.toString(),
                    bind.etKeterangan.text.toString(),
                    stringDateToLong(bind.etMulai.text.toString()),
                    stringDateToLong(bind.etSelesai.text.toString()),
                    bind.etLokasi.text.toString(),
                    latLng.latitude,
                    latLng.longitude
                )
            }
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        maps = googleMap
        setMaps()
        maps.setOnMapClickListener(object:GoogleMap.OnMapClickListener{
            override fun onMapClick(p0: LatLng) {
                latLng = p0
                bind.tvCoordinat.text = "${latLng.latitude}, ${latLng.longitude}"
                setMaps()
            }
        })
    }
    private fun setMaps(){
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
    private fun clearField() {
        latLng = LatLng(-7.7604026, 110.404842)
        setMaps()
        bind.tvCoordinat.text = "${latLng.latitude}, ${latLng.longitude}"
        bind.etJudul.text = null
        bind.etKeterangan.text = null
        bind.etLokasi.text = null
        bind.etMulai.text = null
        bind.etSelesai.text = null

    }
    @SuppressLint("SimpleDateFormat")
    private fun stringDateToLong(stringDate:String):Long{
        return SimpleDateFormat("HH:mm dd MMM yyyy").parse(stringDate).time
    }
    @SuppressLint("SimpleDateFormat")
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