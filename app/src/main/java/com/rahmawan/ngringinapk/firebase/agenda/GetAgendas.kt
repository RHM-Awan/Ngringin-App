package com.rahmawan.ngringinapk.firebase.agenda

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Agenda
import kotlin.collections.ArrayList

object GetAgendas {
    private lateinit var getAgendasListener: GetAgendasListener
    interface GetAgendasListener {
        fun onSuccess(result: ArrayList<Agenda>)
        fun onCancelled(message:String)
    }
    fun getAgendas(){
        var agendas:ArrayList<Agenda>
        Firebase.agendaRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                agendas = ArrayList()
                //var currentTime = Calendar.getInstance().time
                for (dataSnapshot in snapshot.children) {
                    dataSnapshot.getValue(Agenda::class.java)?.let {
                        //if(!Date(it.dateStart!!).after(currentTime)){
                            agendas.add(it)

                        //}
                    }
                }
                getAgendasListener.onSuccess(agendas)
            }
            override fun onCancelled(error: DatabaseError) {
                getAgendasListener.onCancelled(error.message)
            }
        })
    }
    fun setOnAgendasListener(listener: GetAgendasListener) {
        getAgendasListener = listener
    }
}