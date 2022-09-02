package com.rahmawan.ngringinapk.firebase.galeri

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.firebase.user.GetUser
import com.rahmawan.ngringinapk.model.Galeri
import com.rahmawan.ngringinapk.model.Presensi
import com.rahmawan.ngringinapk.model.User


object GetGaleri {
    private lateinit var getGaleriListener: GetGaleriListener
    interface GetGaleriListener {
        fun onSuccess(result: Galeri?)
        fun onFailure(error:String)
    }
    fun getGaleri(id:String){
        Firebase.galeriRef.child(id).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                getGaleriListener.onSuccess(snapshot.getValue(Galeri::class.java))
            }
            override fun onCancelled(error: DatabaseError) {
                getGaleriListener.onFailure(error.message)
            }
        })
    }
    fun setOnGaleriListener(listener: GetGaleriListener) {
        getGaleriListener = listener
    }
}