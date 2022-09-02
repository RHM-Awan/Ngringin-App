package com.rahmawan.ngringinapk.activity.admin

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.rahmawan.ngringinapk.PencapaianUtils
import com.rahmawan.ngringinapk.databinding.ActivityStatistikPencapaianBinding
import com.rahmawan.ngringinapk.firebase.pencapaian.GetPencapaians
import com.rahmawan.ngringinapk.firebase.user.GetUsers
import com.rahmawan.ngringinapk.model.Pencapaian
import com.rahmawan.ngringinapk.model.User

class StatistikPencapaianActivity : AppCompatActivity() {
    private lateinit var bind:ActivityStatistikPencapaianBinding
    private val getUsers= GetUsers
    private val getPencapaians = GetPencapaians
    private var totalAlqruanLaki=0
    private var totalIqroLaki=0
    private var totalAlqruanPerempuan=0
    private var totalIqroPerempuan=0
    private var totalPercentageAlquran=arrayOf(0f,0f)
    private var totalPercentageIqro=arrayOf(0f,0f)
    private var totalUsers=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityStatistikPencapaianBinding.inflate(layoutInflater)
        setContentView(bind.root)
        initListener()
        getUsers.getMurids()
    }
    private fun initListener(){
        getPencapaians.setOnPencapaianListener(object : GetPencapaians.GetPencapaianListener{
            override fun onSuccess(result: ArrayList<Pencapaian>, type: String) {}

            override fun onSuccess(result: java.util.ArrayList<Pencapaian>, type: String, gender: String) {
                if(type == "alquran"){
                    if(gender=="Laki-laki"){
                        totalPercentageAlquran[0]+=PencapaianUtils.percentageAlquran(result).toFloat()
                        totalAlqruanLaki+=1
                    }else{
                        totalPercentageAlquran[1]+=PencapaianUtils.percentageAlquran(result).toFloat()
                        totalAlqruanPerempuan+=1
                    }
                }else{
                    if(gender=="Laki-laki"){
                        totalPercentageIqro[0]+=PencapaianUtils.percentageIqro(result).toFloat()
                        totalIqroLaki+=1
                    }else{
                        totalPercentageIqro[1]+=PencapaianUtils.percentageIqro(result).toFloat()
                        totalIqroPerempuan+=1
                    }
                }
                if(totalAlqruanLaki+totalIqroPerempuan==totalUsers&&totalIqroLaki+totalIqroPerempuan==totalUsers){
                    initData()
                }
            }
            override fun onCancelled(message: String) {
                Toast.makeText(this@StatistikPencapaianActivity,message, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        getUsers.setOnUsersListener(object: GetUsers.GetUsersListener{
            override fun onSuccess(result: ArrayList<User>) {}
            override fun onSuccess(aktif: ArrayList<User>, tidakAktif: ArrayList<User>) {
                totalUsers = aktif.size
                for(user in aktif){
                    getPencapaians.getAlQuran(user.idUser!!,user.gender!!)
                    getPencapaians.getIqro(user.idUser!!,user.gender!!)
                }
            }
            override fun onCancelled(message: String) {
                Toast.makeText(this@StatistikPencapaianActivity,message, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }
    private fun initData(){

        val barEntries1:ArrayList<BarEntry> =ArrayList()
        val barEntries2:ArrayList<BarEntry> =ArrayList()
        barEntries1.add(BarEntry(1f,totalPercentageAlquran[0]/totalAlqruanLaki))
        barEntries1.add(BarEntry(2f,totalPercentageIqro[0]/totalIqroLaki))
        barEntries2.add(BarEntry(1f,totalPercentageAlquran[1]/totalAlqruanPerempuan))
        barEntries2.add(BarEntry(2f,totalPercentageIqro[1]/totalIqroPerempuan))

        val barDataSet1 = BarDataSet(barEntries1, "Laki-laki")
        val barDataSet2 = BarDataSet(barEntries2, "Perempuan")

        barDataSet1.color = Color.BLUE
        barDataSet2.color = Color.YELLOW

        bind.bcPresensi.description.isEnabled = false
        val xAxis: XAxis = bind.bcPresensi.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("Al-Quran","Iqro"))
        xAxis.setCenterAxisLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        bind.bcPresensi.isDragEnabled = true
        bind.bcPresensi.setVisibleXRangeMaximum(4f)
        bind.bcPresensi.xAxis.axisMinimum = 0f
        bind.bcPresensi.animate()
        bind.bcPresensi.invalidate()
        val data = BarData(barDataSet1, barDataSet2)
        data.barWidth = 0.15f
        bind.bcPresensi.data = data
        val barSpace = 0.1f
        val groupSpace = 0.5f
        bind.bcPresensi.groupBars(0f, groupSpace, barSpace)
    }
}