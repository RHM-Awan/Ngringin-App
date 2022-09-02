package com.rahmawan.ngringinapk.firebase.presensi

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Agenda
import com.rahmawan.ngringinapk.model.Presensi

object GetPresensis {
    private lateinit var getPresensisListener: GetPresensisListener
    interface GetPresensisListener {
        fun onSuccess(result: ArrayList<Presensi>)
        fun onCancelled(message:String)
    }
    fun getPresensis(id:String){
        var presensis:ArrayList<Presensi>
        Firebase.presensiRef(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                presensis = ArrayList()
                var i = 1
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.key?.let {
                        presensis.add(Presensi(it,dataSnapshot.value as Long,"${i++}. "))
                    }
                }
                getPresensisListener.onSuccess(presensis)
            }
            override fun onCancelled(error: DatabaseError) {
                getPresensisListener.onCancelled(error.message)
            }
        })
    }
    fun setOnPresensisListener(listener: GetPresensisListener) {
        getPresensisListener = listener
    }
}