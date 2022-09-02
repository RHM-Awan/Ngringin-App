package com.rahmawan.ngringinapk.firebase

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.rahmawan.ngringinapk.model.User

object Firebase {
    val database: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://ngringinapk-default-rtdb.asia-southeast1.firebasedatabase.app")
    val storage = FirebaseStorage.getInstance().reference
    val userRef: DatabaseReference = database.getReference("user")
    val galeriRef: DatabaseReference = database.getReference("galeri")
    val agendaRef: DatabaseReference = database.getReference("agenda")
    val alquranRef: (String) -> DatabaseReference ={
        database.getReference("pencapaian").child(it).child("alquran")
    }
    val iqroRef: (String) -> DatabaseReference ={
        database.getReference("pencapaian").child(it).child("iqro")
    }
    val presensiUserRef: (String, String) -> DatabaseReference ={ uid, id->
        Log.d("TAG", ": $uid")
        database.getReference("presensi").child(id).child(uid)
    }
    val presensiRef: (String) -> DatabaseReference ={
        database.getReference("presensi").child(it)
    }
    val presensiAllRef =database.getReference("presensi")
}