package com.rahmawan.ngringinapk.firebase.user

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Presensi
import com.rahmawan.ngringinapk.model.User

object GetUser {
    private lateinit var getUserListener: GetUserListener
    private lateinit var getUserPresensiListener: GetUserPresensiListener
    interface GetUserListener {
        fun onSuccess(result: User?)
        fun onFailure(error:String)
    }
    interface GetUserPresensiListener {
        fun onSuccess(presensi: Presensi)
        fun onFailure(error:String)
    }
    fun getUser(uid:String){
        Firebase.userRef.child(uid).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                getUserListener.onSuccess(snapshot.getValue(User::class.java))
            }
            override fun onCancelled(error: DatabaseError) {
                getUserListener.onFailure(error.message)
            }
        })
    }
    fun getUser(presensi:Presensi){
        Firebase.userRef.child(presensi.id).addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                presensi.user+=snapshot.getValue(User::class.java)?.name
                getUserPresensiListener.onSuccess(presensi)
            }
            override fun onCancelled(error: DatabaseError) {
                getUserPresensiListener.onFailure(error.message)
            }
        })
    }
    fun setOnUserListener(listener: GetUserListener) {
        getUserListener = listener
    }
    fun setOnUserListener(listener: GetUserPresensiListener) {
        getUserPresensiListener = listener
    }
}