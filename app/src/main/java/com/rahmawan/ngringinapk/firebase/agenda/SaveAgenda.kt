package com.rahmawan.ngringinapk.firebase.agenda

import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Agenda

object SaveAgenda {
    private lateinit var saveAgendaListener: SaveAgendaListener
    interface SaveAgendaListener {
        fun onSuccess(result:String)
        fun onFailure(error:String)
    }
    fun saveAgenda(title:String, body:String, dateStart:Long, dateFinish:Long, address:String, lat:Double, lng:Double){
        val idAgenda: String? = Firebase.agendaRef.push().key
        if(idAgenda!=null){
            val agenda = Agenda(idAgenda,title,body,dateStart,dateFinish,address,lat,lng)
            Firebase.agendaRef.child(idAgenda).setValue(agenda).addOnFailureListener {
               saveAgendaListener.onFailure(it.toString())
            }.addOnSuccessListener {
                saveAgendaListener.onSuccess("Agenda telah ditambahkan")
            }
        }
    }
    fun setOnSaveAgendaListener(listener: SaveAgendaListener) {
        saveAgendaListener = listener
    }
}