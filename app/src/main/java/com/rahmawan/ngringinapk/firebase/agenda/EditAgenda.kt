package com.rahmawan.ngringinapk.firebase.agenda

import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Agenda

object EditAgenda {
    private lateinit var editAgendaListener: EditAgendaListener
    interface EditAgendaListener {
        fun onSuccess(result:String)
        fun onFailure(error:String)
    }
    fun editAgenda(agenda: Agenda){
        Firebase.agendaRef.child(agenda.idAgenda!!).setValue(agenda).addOnFailureListener {
            editAgendaListener.onFailure(it.toString())
        }.addOnSuccessListener {
            editAgendaListener.onSuccess("Edit Berhasil")
        }
    }
    fun setOnEditAgendaListener(listener: EditAgendaListener) {
        editAgendaListener = listener
    }
}