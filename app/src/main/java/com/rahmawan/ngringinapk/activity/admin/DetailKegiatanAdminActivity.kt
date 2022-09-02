package com.rahmawan.ngringinapk.activity.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.databinding.ActivityDetailKegiatanAdminBinding
import com.rahmawan.ngringinapk.firebase.agenda.GetAgenda
import com.rahmawan.ngringinapk.fragment.admin.KegiatanFragment
import com.rahmawan.ngringinapk.fragment.admin.ListPresensiFragment
import com.rahmawan.ngringinapk.model.Agenda

class DetailKegiatanAdminActivity : AppCompatActivity() {
    private lateinit var bind: ActivityDetailKegiatanAdminBinding
    private lateinit var id:String
    private var fragment:Fragment= KegiatanFragment(Agenda())
    private var agenda=Agenda()
    private val getAgenda= GetAgenda
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityDetailKegiatanAdminBinding.inflate(layoutInflater)
        setContentView(bind.root)
        id= intent.extras?.get("id").toString()
        bind.tlKegiatan.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> fragment= KegiatanFragment(agenda)
                    1 -> fragment= ListPresensiFragment(agenda)
                }
                supportFragmentManager.beginTransaction().replace(R.id.fl_kegiatan, fragment).commit()
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        initListener()
        getAgenda.getAgenda(id)
    }
    private fun initListener(){
        getAgenda.setOnAgendaListener(object: GetAgenda.GetAgendaListener{
            override fun onSuccess(result: Agenda?) {
                result?.let {
                    agenda=it
                    title=it.title
                    refreshFragment()
                }?:run{
                    Toast.makeText(this@DetailKegiatanAdminActivity,"Data Telah Dihapus", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@DetailKegiatanAdminActivity,error, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun refreshFragment(){
        if(fragment is KegiatanFragment){
            fragment = KegiatanFragment(agenda)
        }else if(fragment is ListPresensiFragment){
            fragment = ListPresensiFragment(agenda)
        }
        supportFragmentManager.beginTransaction().replace(R.id.fl_kegiatan, fragment).commit()
    }
}