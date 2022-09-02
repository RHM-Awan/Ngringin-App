package com.rahmawan.ngringinapk.firebase.presensi

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.rahmawan.ngringinapk.firebase.Firebase

object GetPresensi {
    private lateinit var getPresensiListener: GetPresensiListener
    interface GetPresensiListener {
        fun onSuccess(result:Long?)
        fun onFailure(error:String)
    }
    fun getPresensi(uid:String,id:String){
        Firebase.presensiUserRef(uid,id).addValueEventListener (object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.value?.let {
                    getPresensiListener.onSuccess(it as Long)
                }?:run {
                    getPresensiListener.onSuccess(null)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                getPresensiListener.onFailure(error.message)
            }
        })
    }
    fun setOnPresensiListener(listener: GetPresensiListener) {
        getPresensiListener = listener
    }
}