package com.rahmawan.ngringinapk.firebase.user

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.User

object GetUsers {
    private lateinit var getUsersListener: GetUsersListener
    interface GetUsersListener {
        fun onSuccess(result: ArrayList<User>)
        fun onSuccess(result1: ArrayList<User>,result2: ArrayList<User>)
        fun onCancelled(message:String)
    }
    fun getUsers(){
        var users:ArrayList<User>
        Firebase.userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users = ArrayList()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(User::class.java)?.let {
                        users.add(it)
                    }
                }
                getUsersListener.onSuccess(users)
            }
            override fun onCancelled(error: DatabaseError) {
                getUsersListener.onCancelled(error.message)
            }
        })
    }
    fun getMurids(){
        var aktif:ArrayList<User>
        var tidakAktif:ArrayList<User>
        Firebase.userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                aktif = ArrayList()
                tidakAktif = ArrayList()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(User::class.java)?.let {
                        if(it.typeAccount=="user"){
                            if(it.statusActive=="aktif"){
                                aktif.add(it)
                            }else{
                                tidakAktif.add(it)
                            }
                        }
                    }
                }
                getUsersListener.onSuccess(aktif,tidakAktif)
            }
            override fun onCancelled(error: DatabaseError) {
                getUsersListener.onCancelled(error.message)
            }
        })
    }
    fun setOnUsersListener(listener: GetUsersListener) {
        getUsersListener = listener
    }
}