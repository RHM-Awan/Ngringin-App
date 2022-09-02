package com.rahmawan.ngringinapk.activity.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.rahmawan.ngringinapk.adapter.SliderAdapter
import com.rahmawan.ngringinapk.databinding.ActivityDetailGaleriUserBinding
import com.rahmawan.ngringinapk.firebase.galeri.GetGaleri
import com.rahmawan.ngringinapk.firebase.user.GetUser
import com.rahmawan.ngringinapk.model.Galeri
import com.rahmawan.ngringinapk.model.User
import java.text.SimpleDateFormat
import java.util.*

class DetailGaleriUserActivity : AppCompatActivity() {
    private lateinit var bind: ActivityDetailGaleriUserBinding
    private val getGaleri= GetGaleri
    private val getUser= GetUser
    private lateinit var id:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailGaleriUserBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        id=intent.extras?.getString("id").toString()
        initListener()
        getGaleri.getGaleri(id)
    }

    private fun initListener(){
        getGaleri.setOnGaleriListener(object:GetGaleri.GetGaleriListener{
            override fun onSuccess(result: Galeri?) {
                result?.let {
                    initData(it)
                    getUser.getUser(it.idUser!!)
                }?:run{
                    Toast.makeText(this@DetailGaleriUserActivity,"Data Telah Dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@DetailGaleriUserActivity,error, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        getUser.setOnUserListener(object :GetUser.GetUserListener{
            override fun onSuccess(result: User?) {
                bind.tvCreator.text=result?.name
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@DetailGaleriUserActivity,error, Toast.LENGTH_SHORT).show()
            }
        })
        bind.vpSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bind.tlSlider.selectTab(bind.tlSlider.getTabAt(position))
            }
        })
        bind.tlSlider.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                bind.vpSlider.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
    @SuppressLint("SimpleDateFormat")
    private fun initData(galeri: Galeri){
        title=galeri.judul
        bind.tvJudul.text=galeri.judul
        bind.tvDetail.text=galeri.deskripsi
        bind.tvTanggal.text= SimpleDateFormat("dd MMMM yyyy").format(Date(galeri.tanggal!!))
        val gambars: ArrayList<String> = ArrayList()
        bind.tlSlider.removeAllTabs()
        if(galeri.gambar1!=null){
            bind.tlSlider.addTab(bind.tlSlider.newTab())
            gambars.add(galeri.gambar1!!)
        }
        if(galeri.gambar2!=null){
            bind.tlSlider.addTab(bind.tlSlider.newTab())
            gambars.add(galeri.gambar2!!)
        }
        if(galeri.gambar3!=null){
            bind.tlSlider.addTab(bind.tlSlider.newTab())
            gambars.add(galeri.gambar3!!)
        }
        bind.vpSlider.adapter= SliderAdapter(gambars)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}