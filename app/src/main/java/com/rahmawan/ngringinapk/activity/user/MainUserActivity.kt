package com.rahmawan.ngringinapk.activity.user

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.activity.LoginActivity
import com.rahmawan.ngringinapk.activity.ProfilActivity
import com.rahmawan.ngringinapk.activity.service.AgendaNotificationService
import com.rahmawan.ngringinapk.databinding.ActivityMainUserBinding
import com.rahmawan.ngringinapk.fragment.user.AgendaUserFragment
import com.rahmawan.ngringinapk.fragment.user.GaleriUserFragment
import com.rahmawan.ngringinapk.fragment.user.PresensiUserFragment

class MainUserActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainUserBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var iService: Intent
    private var fragment: Fragment = AgendaUserFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainUserBinding.inflate(layoutInflater)
        setContentView(bind.root)
        prefs=getSharedPreferences("user", MODE_PRIVATE)
        title="Agenda"
        initListener()
        iService = Intent(this, AgendaNotificationService::class.java)
        startService(iService)
    }

    override fun onResume() {
        super.onResume()
        refreshFragment()
    }
    private fun refreshFragment(){
        if(fragment is AgendaUserFragment){
            fragment =AgendaUserFragment()
        }else if(fragment is PresensiUserFragment){
            fragment =PresensiUserFragment()
        }else if(fragment is GaleriUserFragment){
            fragment =GaleriUserFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.fl_user, fragment).commit()
    }
    fun initListener(){
        bind.bnvUser.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.agenda -> {
                    title="Agenda"
                    fragment=AgendaUserFragment()
                }
                R.id.presensi-> {
                    title="Presensi"
                    fragment=PresensiUserFragment()
                }
                R.id.galeri -> {
                    title="Galeri"
                    fragment=GaleriUserFragment()
                }
                R.id.profil-> {
                    startActivity(Intent(this,ProfilActivity::class.java))
                    return@setOnItemSelectedListener false
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.fl_user, fragment).commit()
            return@setOnItemSelectedListener true
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_kebab_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                prefs.edit().remove("uid").apply()
                startActivity(Intent(this, LoginActivity::class.java))
                stopService(iService)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}