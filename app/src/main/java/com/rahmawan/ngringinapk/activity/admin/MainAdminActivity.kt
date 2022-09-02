package com.rahmawan.ngringinapk.activity.admin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.rahmawan.ngringinapk.R
import com.rahmawan.ngringinapk.activity.LoginActivity
import com.rahmawan.ngringinapk.activity.ProfilActivity
import com.rahmawan.ngringinapk.databinding.ActivityMainAdminBinding
import com.rahmawan.ngringinapk.databinding.AdminSideHeaderBinding
import com.rahmawan.ngringinapk.firebase.user.GetUser
import com.rahmawan.ngringinapk.fragment.admin.AgendaAdminFragment
import com.rahmawan.ngringinapk.fragment.admin.GaleriAdminFragment
import com.rahmawan.ngringinapk.fragment.admin.PresensiAdminFragment
import com.rahmawan.ngringinapk.model.User

class MainAdminActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainAdminBinding
    private lateinit var bindHeader: AdminSideHeaderBinding
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var prefs: SharedPreferences
    private lateinit var uid:String
    private lateinit var getUser: GetUser
    private var fragment: Fragment = AgendaAdminFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind =ActivityMainAdminBinding.inflate(layoutInflater)
        bindHeader = AdminSideHeaderBinding.bind(bind.nvAdmin.getHeaderView(0))
        toolbar = bind.toolbarAdmin
        setSupportActionBar(toolbar)
        setContentView(bind.root)
        title="Agenda"
        initListener()
        prefs= getSharedPreferences("user", MODE_PRIVATE)
        uid = prefs.getString("uid", "-")!!
    }
    override fun onResume() {
        getUser= GetUser
        getUser.setOnUserListener(object: GetUser.GetUserListener{
            override fun onSuccess(result: User?) {
                result?.let {
                    initHeader(it)
                }?:run{
                    Toast.makeText(this@MainAdminActivity,"Terjadi Kesalahan, Coba Lagi", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            override fun onFailure(error: String) {
                Toast.makeText(this@MainAdminActivity,error, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        getUser.getUser(uid)
        refreshFragment()
        super.onResume()
    }
    private fun refreshFragment(){
        if(fragment is AgendaAdminFragment){
            fragment =AgendaAdminFragment()
        }else if(fragment is PresensiAdminFragment){
            fragment=PresensiAdminFragment()
        }else if(fragment is GaleriAdminFragment){
            fragment=GaleriAdminFragment()
        }
        supportFragmentManager.beginTransaction().replace(R.id.fl_admin, fragment).commit()
    }
    private fun initListener(){
        bind.nvAdmin.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.murid -> {
                    startActivity(Intent(this,UsersActivity::class.java))
                }
                R.id.stat_presensi-> {
                    startActivity(Intent(this,StatistikPresensiActivity::class.java))
                }
                R.id.stat_pencapaian -> {
                    startActivity(Intent(this,StatistikPencapaianActivity::class.java))
                }
            }
            true
        }
        bind.bnvAdmin.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.agenda -> {
                    title="Agenda"
                    fragment=AgendaAdminFragment()
                }
                R.id.presensi-> {
                    title="Presensi"
                    fragment= PresensiAdminFragment()
                }
                R.id.galeri -> {
                    title="Galeri"
                    fragment= GaleriAdminFragment()
                }
                R.id.profil-> {
                    startActivity(Intent(this, ProfilActivity::class.java))
                    return@setOnItemSelectedListener false
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.fl_admin, fragment).commit()
            return@setOnItemSelectedListener true
        }
        val toggle = ActionBarDrawerToggle(
            this,
            bind.dlAdmin,
            toolbar,
            R.string.navigation_open_drawer,
            R.string.navigation_close_drawer
        )
        bind.dlAdmin.addDrawerListener(toggle)
        toggle.syncState()
    }
    private fun initHeader(user:User){
        bindHeader.tvNama.text=user.name
        Glide.with(this)
            .load(if(user.image.equals("-")) R.drawable.nophoto else user.image)
            .into(bindHeader.ivProfil)
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
            }
        }
        return super.onOptionsItemSelected(item)
    }
}