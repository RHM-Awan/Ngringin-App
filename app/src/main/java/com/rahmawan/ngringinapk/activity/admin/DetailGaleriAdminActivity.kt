package com.rahmawan.ngringinapk.activity.admin

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.adapter.SliderAdapter
import com.rahmawan.ngringinapk.databinding.ActivityDetailGaleriAdminBinding
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.firebase.galeri.GetGaleri
import com.rahmawan.ngringinapk.firebase.user.GetUser
import com.rahmawan.ngringinapk.model.Galeri
import com.rahmawan.ngringinapk.model.User
import java.text.SimpleDateFormat
import java.util.*

class DetailGaleriAdminActivity : AppCompatActivity() {
    private lateinit var bind: ActivityDetailGaleriAdminBinding
    private lateinit var getGaleri:GetGaleri
    private lateinit var getUser: GetUser
    private lateinit var id:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailGaleriAdminBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        id=intent.extras?.getString("id").toString()
        initListener()
    }
    override fun onResume() {
        super.onResume()
        getGaleri=GetGaleri
        getGaleri.setOnGaleriListener(object:GetGaleri.GetGaleriListener{
            override fun onSuccess(result: Galeri?) {
                result?.let {
                    initData(it)
                    getUser.getUser(it.idUser!!)
                }?:run{
                    Toast.makeText(this@DetailGaleriAdminActivity,"Data Telah Dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@DetailGaleriAdminActivity,error, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        getGaleri.getGaleri(id)
        getUser=GetUser
        getUser.setOnUserListener(object :GetUser.GetUserListener{
            override fun onSuccess(result: User?) {
                bind.tvCreator.text=result?.name
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@DetailGaleriAdminActivity,error,Toast.LENGTH_SHORT).show()
            }
        })
    }

    val dialogClickListener =
        DialogInterface.OnClickListener{ dialog, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {Firebase.galeriRef.child(id).removeValue()}
                DialogInterface.BUTTON_NEGATIVE -> {}
            }
        }

    private fun initListener(){
        val  builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(bind.btnHapus.context)
        bind.btnHapus.setOnClickListener(View.OnClickListener {
            builder.setMessage("Yakin ingin menghapus gallery?").setPositiveButton(R.string.hapus, dialogClickListener)
                .setNeutralButton(R.string.batal, dialogClickListener).show()
            return@OnClickListener
        })
//        bind.btnHapus.setOnClickListener {
//            Firebase.galeriRef.child(id).removeValue()
//        }
        bind.btnEdit.setOnClickListener{
            startActivity(Intent(this,EditGaleriActivity::class.java).putExtra("id",id))
        }
        bind.vpSlider.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bind.tlSlider.selectTab(bind.tlSlider.getTabAt(position))
            }
        })
        bind.tlSlider.addOnTabSelectedListener(object : OnTabSelectedListener {
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
        bind.tvTanggal.text=SimpleDateFormat("dd MMMM yyyy").format(Date(galeri.tanggal!!))
        val gambars:ArrayList<String> = ArrayList()
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
        bind.vpSlider.adapter=SliderAdapter(gambars)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

//    fun showAlertDialog(view: View){
//       // getGaleri.getGaleri(judul)
//        val a = getGaleri.getGaleri(judul)
//        MaterialAlertDialogBuilder(this)
//            .setMessage("yakin ingin menghapus ${a}?")
//            .setPositiveButton("Yes"){
//                dialog, which ->
//                Firebase.galeriRef.child(id).removeValue()
//            }
//            .setNegativeButton("No"){
//                    dialog, which ->
//            }
//            .show()
//    }

}