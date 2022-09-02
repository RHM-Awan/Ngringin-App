package com.rahmawan.ngringinapk.firebase.pencapaian

import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Pencapaian

object EditPencapaian {
    private lateinit var editPencapaianListener: EditPencapaianListener
    interface EditPencapaianListener {
        fun onSuccess(result:String)
        fun onFailure(error:String)
    }
    fun editAlquran(uid:String, pencapaian: Pencapaian){
        Firebase.alquranRef(uid).child(pencapaian.no.toString()).setValue(pencapaian).addOnFailureListener {
            editPencapaianListener.onFailure(it.toString())
        }.addOnSuccessListener {
            editPencapaianListener.onSuccess("Edit Al-Quran Berhasil")
        }
    }
    fun editIqro(uid:String, pencapaian: Pencapaian){
        Firebase.iqroRef(uid).child(pencapaian.no.toString()).setValue(pencapaian).addOnFailureListener {
            editPencapaianListener.onFailure(it.toString())
        }.addOnSuccessListener {
            editPencapaianListener.onSuccess("Edit Iqro Berhasil")
        }
    }
    fun setOnEditPencapaianListener(listener: EditPencapaianListener) {
        editPencapaianListener = listener
    }
}