package com.rahmawan.ngringinapk.activity

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.databinding.ActivityProfilBinding
import com.rahmawan.ngringinapk.fragment.BiodataFragment
import com.rahmawan.ngringinapk.fragment.PencapaianFragment

class ProfilActivity : AppCompatActivity() {
    private lateinit var bind: ActivityProfilBinding
    private lateinit var prefs: SharedPreferences
    var fragment: Fragment = BiodataFragment()
    lateinit var uid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(bind.root)
        title="Profil"
        prefs= getSharedPreferences("user", MODE_PRIVATE)
        uid = prefs.getString("uid", "-")!!
        supportFragmentManager.beginTransaction().replace(R.id.fl_profil, fragment).commit()
        bind.tlProfil.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> fragment=BiodataFragment()
                    1 -> fragment=PencapaianFragment()
                }
                supportFragmentManager.beginTransaction().replace(R.id.fl_profil, fragment).commit()
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onResume() {
        super.onResume()
        if(fragment is BiodataFragment){
            fragment = BiodataFragment()
        }else{
            fragment = PencapaianFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.fl_profil, fragment).commit()
    }
}