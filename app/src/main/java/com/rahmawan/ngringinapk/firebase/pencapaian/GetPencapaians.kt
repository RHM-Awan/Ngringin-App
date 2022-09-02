package com.rahmawan.ngringinapk.firebase.pencapaian

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Pencapaian

object GetPencapaians {
    private lateinit var getPencapaianListener: GetPencapaianListener
    interface GetPencapaianListener {
        fun onSuccess(result: ArrayList<Pencapaian>,type:String)
        fun onSuccess(result: ArrayList<Pencapaian>,type:String, gender:String)
        fun onCancelled(message:String)
    }
    fun getAlQuran(uid:String){
        var alqurans:ArrayList<Pencapaian>
        Firebase.alquranRef(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                alqurans = ArrayList()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(Pencapaian::class.java)?.let {
                        alqurans.add(it)
                    }
                }
                getPencapaianListener.onSuccess(alqurans,"alquran")
            }
            override fun onCancelled(error: DatabaseError) {
                getPencapaianListener.onCancelled(error.message)
            }
        })
    }
    fun getIqro(uid:String){
        var iqros:ArrayList<Pencapaian>
        Firebase.iqroRef(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                iqros = ArrayList()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(Pencapaian::class.java)?.let {
                        iqros.add(it)
                    }
                }
                getPencapaianListener.onSuccess(iqros,"iqro")
            }
            override fun onCancelled(error: DatabaseError) {
                getPencapaianListener.onCancelled(error.message)
            }
        })
    }
    fun getAlQuran(uid:String, gender:String){
        var alqurans:ArrayList<Pencapaian>
        Firebase.alquranRef(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                alqurans = ArrayList()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(Pencapaian::class.java)?.let {
                        alqurans.add(it)
                    }
                }
                getPencapaianListener.onSuccess(alqurans,"alquran", gender)
            }
            override fun onCancelled(error: DatabaseError) {
                getPencapaianListener.onCancelled(error.message)
            }
        })
    }
    fun getIqro(uid:String,gender:String){
        var iqros:ArrayList<Pencapaian>
        Firebase.iqroRef(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                iqros = ArrayList()
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(Pencapaian::class.java)?.let {
                        iqros.add(it)
                    }
                }
                getPencapaianListener.onSuccess(iqros,"iqro", gender)
            }
            override fun onCancelled(error: DatabaseError) {
                getPencapaianListener.onCancelled(error.message)
            }
        })
    }
    fun setOnPencapaianListener(listener: GetPencapaianListener) {
        getPencapaianListener = listener
    }
}