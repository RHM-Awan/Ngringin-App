package com.rahmawan.ngringinapk.firebase.galeri

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Galeri
import com.rahmawan.ngringinapk.model.User

object GetGaleris {
    private lateinit var getGalerisListener: GetGalerisListener
    interface GetGalerisListener {
        fun onSuccess(result: ArrayList<Galeri>)
        fun onCancelled(message:String)
    }
    fun getGaleris(){
        var galeris:ArrayList<Galeri>
        Firebase.galeriRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                galeris = ArrayList()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(Galeri::class.java)?.let {
                        galeris.add(it)
                    }
                }
                getGalerisListener.onSuccess(galeris)
            }
            override fun onCancelled(error: DatabaseError) {
                getGalerisListener.onCancelled(error.message)
            }
        })
    }
    fun setOnGalerisListener(listener: GetGalerisListener) {
        getGalerisListener = listener
    }
}