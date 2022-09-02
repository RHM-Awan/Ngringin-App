package com.rahmawan.ngringinapk.firebase.user

import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.User

object EditUser {
    private lateinit var editUserListener: EditUserListener
    interface EditUserListener {
        fun onSuccess(result:String)
        fun onFailure(error:String)
    }
    fun editUser(user: User){
        Firebase.userRef.child(user.idUser!!).setValue(user).addOnFailureListener {
            editUserListener.onFailure(it.toString())
        }.addOnSuccessListener {
            editUserListener.onSuccess("Edit Berhasil")
        }
    }
    fun setOnEditUserListener(listener: EditUserListener) {
        editUserListener = listener
    }
}