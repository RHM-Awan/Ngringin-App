package com.rahmawan.ngringinapk.activity.admin

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.rahmawan.ngringinapk.databinding.ActivityStatistikPresensiBinding
import com.rahmawan.ngringinapk.firebase.agenda.GetAgendas
import com.rahmawan.ngringinapk.firebase.presensi.GetAllPresensi
import com.rahmawan.ngringinapk.firebase.user.GetUsers
import com.rahmawan.ngringinapk.model.Agenda
import com.rahmawan.ngringinapk.model.User
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StatistikPresensiActivity : AppCompatActivity() {
    private lateinit var bind:ActivityStatistikPresensiBinding
    private lateinit var barDataSet1 : BarDataSet
    private lateinit var barDataSet2 : BarDataSet
    private lateinit var barDataSet3 : BarDataSet
    private val barEntries1:ArrayList<BarEntry> =ArrayList()
    private val barEntries2:ArrayList<BarEntry> =ArrayList()
    private val barEntries3:ArrayList<BarEntry> =ArrayList()
    private val getUsers=GetUsers
    private val getAgendas=GetAgendas
    private val getAllPresensi=GetAllPresensi
    private var totalMurid:Int = 0
    private val curTimeGlobal = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityStatistikPresensiBinding.inflate(layoutInflater)
        setContentView(bind.root)
        initListener()
        getUsers.getMurids()
    }

    private fun initListener(){
        getAllPresensi.setOnAllPresensiListener(object :GetAllPresensi.GetPresensisListener{
            override fun onSuccess(masuk: Array<Float>, izin: Array<Float>) {
                barEntries1.clear()
                barEntries2.clear()
                barEntries3.clear()
                for(i in 0..5){
                    barEntries1.add(BarEntry((i+1f),masuk[i]))
                    barEntries2.add(BarEntry((i+1f),izin[i]))
                    barEntries3.add(BarEntry((i+1f),100-(masuk[i]+izin[i])))
                }
                initData()
            }
            override fun onCancelled(message: String) {

            }
        })
        getAgendas.setOnAgendasListener(object :GetAgendas.GetAgendasListener{
            override fun onSuccess(result: ArrayList<Agenda>) {
                getAllPresensi.getAllPresensi(curTimeGlobal.time.month, curTimeGlobal.time.year,totalMurid,result)
            }
            override fun onCancelled(message: String) {
                Toast.makeText(this@StatistikPresensiActivity,message, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        getUsers.setOnUsersListener(object: GetUsers.GetUsersListener{
            override fun onSuccess(result: ArrayList<User>) {}
            override fun onSuccess(aktif: ArrayList<User>, tidakAktif: ArrayList<User>) {
                totalMurid = aktif.size+tidakAktif.size
                getAgendas.getAgendas()
            }
            override fun onCancelled(message: String) {
                Toast.makeText(this@StatistikPresensiActivity,message, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private fun initData(){
        barDataSet1 = BarDataSet(barEntries1, "Masuk")
        barDataSet2 = BarDataSet(barEntries2, "Izin")
        barDataSet3 = BarDataSet(barEntries3, "Alfa")

        barDataSet1.color = Color.GREEN
        barDataSet2.color = Color.BLUE
        barDataSet3.color = Color.RED
        bind.bcPresensi.description.isEnabled = false
        val xAxis: XAxis = bind.bcPresensi.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(getLast5Month())
        xAxis.setCenterAxisLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        bind.bcPresensi.isDragEnabled = true
        bind.bcPresensi.setVisibleXRangeMaximum(4f)
        bind.bcPresensi.xAxis.axisMinimum = 0f
        bind.bcPresensi.animate()
        bind.bcPresensi.invalidate()
        val data = BarData(barDataSet1, barDataSet2, barDataSet3)
        data.barWidth = 0.15f
        bind.bcPresensi.data = data
        val barSpace = 0.1f
        val groupSpace = 0.25f
        bind.bcPresensi.groupBars(0f, groupSpace, barSpace)
    }

    private fun getLast5Month(): ArrayList<String> {
        val months = ArrayList<String>()
        val dateFormatter = SimpleDateFormat("MMM yyyy")
        var currentTime = curTimeGlobal
        currentTime.add(Calendar.MONTH,-5)
        months.add(dateFormatter.format(currentTime.time))
        currentTime.add(Calendar.MONTH,1)
        months.add(dateFormatter.format(currentTime.time))
        currentTime.add(Calendar.MONTH,1)
        months.add(dateFormatter.format(currentTime.time))
        currentTime.add(Calendar.MONTH,1)
        months.add(dateFormatter.format(currentTime.time))
        currentTime.add(Calendar.MONTH,1)
        months.add(dateFormatter.format(currentTime.time))
        currentTime.add(Calendar.MONTH,1)
        months.add(dateFormatter.format(currentTime.time))
        return months
    }
}