package com.rahmawan.ngringinapk.activity.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.rahmawan.ngringinapk.adapter.MuridAdapter
import com.rahmawan.ngringinapk.databinding.ActivityUsersBinding
import com.rahmawan.ngringinapk.firebase.user.GetUsers
import com.rahmawan.ngringinapk.model.User

class UsersActivity : AppCompatActivity() {
    private lateinit var bind: ActivityUsersBinding
    private lateinit var getUsers: GetUsers
    private var aktif:ArrayList<User> = ArrayList()
    private var tidakAktif:ArrayList<User> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(bind.root)
        title="Murid"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bind.rvMurid.layoutManager = LinearLayoutManager(this)
        bind.tlMurid.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> bind.rvMurid.adapter = MuridAdapter(aktif)
                    1 -> bind.rvMurid.adapter = MuridAdapter(tidakAktif)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        initListener()
    }
    private fun initListener() {

    }
    override fun onResume() {
        getUsers= GetUsers
        getUsers.setOnUsersListener(object : GetUsers.GetUsersListener {
            override fun onSuccess(result: ArrayList<User>) {}

            override fun onSuccess(result1: ArrayList<User>, result2: ArrayList<User>) {
                aktif=result1
                val a = result1.sortBy {
                    it.name
                }
                tidakAktif=result2
                when (bind.tlMurid.selectedTabPosition) {
                    0 -> bind.rvMurid.adapter = MuridAdapter(aktif)
                    1 -> bind.rvMurid.adapter = MuridAdapter(tidakAktif)
                }
                return a
            }

            override fun onCancelled(message: String) {
                Toast.makeText(
                    this@UsersActivity,
                    "Terjadi Kesalahan, Coba lagi",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        getUsers.getMurids()
        super.onResume()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}