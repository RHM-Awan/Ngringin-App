package com.rahmawan.ngringinapk.firebase.user

import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Pencapaian
import com.rahmawan.ngringinapk.model.User

object SaveUser {
    private lateinit var saveUserListener: SaveUserListener
    interface SaveUserListener {
        fun onSuccess(result:String)
        fun onFailure(error:String)
    }
    fun saveUser(name:String, email:String, password:String, birthday:Long, phone:String, gender:String, address:String){
        val idUser: String? = Firebase.userRef.push().key
        if(idUser!=null){
            val user = User(idUser,name,email,password,birthday,phone,gender,"-","user","aktif",address)
            Firebase.userRef.child(idUser).setValue(user).addOnFailureListener {
                saveUserListener.onFailure(it.toString())
            }.addOnSuccessListener {
                for (i in 1..30) {
                    Firebase.alquranRef(idUser).child(i.toString()).setValue(Pencapaian(i,0))
                }
                for (i in 1..6) {
                    Firebase.iqroRef(idUser).child(i.toString()).setValue(Pencapaian(i,0))
                }
                saveUserListener.onSuccess("Register Berhasil!")
            }
        }
    }
    fun setOnSaveUserListener(listener: SaveUserListener) {
        saveUserListener = listener
    }
}