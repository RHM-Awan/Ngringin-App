package com.rahmawan.ngringinapk.firebase.agenda

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Agenda

object GetAgenda {
    private lateinit var getAgendaListener: GetAgendaListener
    interface GetAgendaListener {
        fun onSuccess(result: Agenda?)
        fun onFailure(error:String)
    }
    fun getAgenda(id:String){
        Firebase.agendaRef.child(id).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                getAgendaListener.onSuccess(snapshot.getValue(Agenda::class.java))
            }
            override fun onCancelled(error: DatabaseError) {
                getAgendaListener.onFailure(error.message)
            }
        })
    }
    fun setOnAgendaListener(listener: GetAgendaListener) {
        getAgendaListener = listener
    }
}