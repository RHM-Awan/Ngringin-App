package com.rahmawan.ngringinapk.firebase.agenda

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Agenda

object GetNewAgenda {
    private lateinit var getNewAgendaListener: GetNewAgendaListener
    private lateinit var listener:ChildEventListener
    private var isNew = false
    interface GetNewAgendaListener {
        fun onSuccess(result: Agenda)
        fun onCancelled(message:String)
    }
    fun getNewAgenda() {
        listener = Firebase.agendaRef.limitToLast(1).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(isNew) {
                    snapshot.getValue(Agenda::class.java)
                        ?.let { getNewAgendaListener.onSuccess(it) }
                }
                isNew = true
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }
    fun setOnNewAgendasListener(listener: GetNewAgendaListener) {
        getNewAgendaListener = listener
    }
}