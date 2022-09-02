package com.rahmawan.ngringinapk.activity.user

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.rahmawan.ngringinapk.databinding.ActivityDetailKegiatanUserBinding
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.firebase.agenda.GetAgenda
import com.rahmawan.ngringinapk.firebase.presensi.GetPresensi
import com.rahmawan.ngringinapk.model.Agenda
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DetailKegiatanUserActivity : AppCompatActivity(), LocationListener {
    private lateinit var bind: ActivityDetailKegiatanUserBinding
    val locationEnd = Location("point End")
    val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private lateinit var locationManager: LocationManager
    private val getAgenda= GetAgenda
    private val getPresensi= GetPresensi
    private lateinit var id:String
    private lateinit var uid:String
    private lateinit var agenda:Agenda
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailKegiatanUserBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        id=intent.extras?.getString("id").toString()
        val prefs= getSharedPreferences("user", MODE_PRIVATE)
        uid = prefs.getString("uid", "-")!!
        initListener()
        getAgenda.getAgenda(id)
        getPresensi.getPresensi(uid,id)
    }
    private fun getLoc(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            }
        }
        try {
            locationManager =applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)

        } catch (e: Exception) {
            Toast.makeText(this, "Terjadi Kesalahan GPS\n${e.message}",Toast.LENGTH_SHORT).show()
        }
    }
    private fun initListener(){
        getAgenda.setOnAgendaListener(object: GetAgenda.GetAgendaListener{
            override fun onSuccess(result: Agenda?) {
                result?.let {
                    initData(it)
                    agenda=it
                }?:run{
                    Toast.makeText(this@DetailKegiatanUserActivity,"Data Telah Dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@DetailKegiatanUserActivity,error, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        getPresensi.setOnPresensiListener(object :GetPresensi.GetPresensiListener{
            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun onSuccess(result: Long?) {
                if(result!=null){
                    if(result==0L){
                        bind.tvInfo.text="Anda Izin"
                    }else {
                        var keterangan: String
                        if (Date(result).after(Date(agenda.dateStart!!))) {
                            keterangan = "Anda Terlambat ${
                                TimeUnit.MINUTES.convert(
                                    result - agenda.dateStart!!,
                                    TimeUnit.MILLISECONDS
                                )
                            } Menit"
                        } else {
                            keterangan = "Anda Datang Tepat Waktu"
                        }
                        bind.tvInfo.text = """
                        |Waktu Presensi ${SimpleDateFormat("HH:mm").format(Date(result))}
                        |$keterangan
                        """.trimMargin()
                        if (this@DetailKegiatanUserActivity::locationManager.isInitialized) {
                            locationManager.removeUpdates(this@DetailKegiatanUserActivity)
                        }
                    }
                }else{
                    with(Calendar.getInstance().time){
                        if(this.after(Date(agenda.dateFinish!!))){
                            bind.tvInfo.text="Absen Gagal, Kegiatan Telah Selesai"
                        }else {
                            val cal = Calendar.getInstance()
                            cal.time = Date(agenda.dateStart!!)
                            cal.add(Calendar.MINUTE, -10);
                            if(this.before(cal.time)){
                                bind.tvInfo.text="Kegiatan belum dibuka\nKegiatan dibuka 10 Menit sebelum dimulai"
                            }else{
                                getLoc()
                            }
                        }
                    }

                }
            }
            override fun onFailure(error: String) {
                Log.d("TAG", error)
            }
        })
    }
    private fun initData(agenda: Agenda){
        title=agenda.title
        bind.tvJudul.text=agenda.title
        bind.tvKeterangan.text=agenda.body
        bind.tvLokasi.text=agenda.address
        bind.tvMulai.text= SimpleDateFormat("HH:mm dd MMM yyyy").format(Date(agenda.dateStart!!))
        bind.tvSelesai.text= SimpleDateFormat("HH:mm dd MMM yyyy").format(Date(agenda.dateFinish!!))
        locationEnd.latitude=agenda.latitude!!
        locationEnd.longitude=agenda.longtitude!!
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
           MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }
        }
    }
    override fun onLocationChanged(location: Location) {
        val distance = location.distanceTo(locationEnd)
        // pengaturan jarak untuk presensi
        if(distance>25){
            bind.tvInfo.text="Jarak : ${distance} meter"
        }else{
            Firebase.presensiUserRef(uid,id).setValue(Calendar.getInstance().time.time)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}