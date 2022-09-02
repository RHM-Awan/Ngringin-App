package com.rahmawan.ngringinapk.firebase.presensi

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Agenda
import com.rahmawan.ngringinapk.model.Presensi
import java.util.*
import kotlin.collections.ArrayList

object GetAllPresensi{
    private lateinit var getAllPresensiListener: GetPresensisListener
    interface GetPresensisListener {
        fun onSuccess(masuk: Array<Float>, izin: Array<Float>)
        fun onCancelled(message:String)
    }
    fun getAllPresensi(month: Int, year:Int,total:Int, agendas:ArrayList<Agenda>){
        Firebase.presensiAllRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalAgenda = arrayOf(0f,0f,0f,0f,0f,0f)
                val masuk = arrayOf(0f,0f,0f,0f,0f,0f)
                val izin = arrayOf(0f,0f,0f,0f,0f,0f)
                for (agenda in snapshot.children) {
                    for(agendaData in agendas){
                        if(agenda.key==agendaData.idAgenda){
                            with(Date(agendaData.dateStart!!)){
                                if(this.year==year){
                                    when(this.month){
                                        (month-5)->{
                                            var izintemp = 0f
                                            var masuktemp = 0f
                                            for(presensi in agenda.children){
                                                if(presensi.value==0L){
                                                    izintemp+=1f
                                                }else{
                                                    masuktemp+=1f
                                                }
                                            }
                                            izin[0]+=(izintemp/total)*100
                                            masuk[0]+=(masuktemp/total)*100
                                            totalAgenda[0]+=1f
                                        }
                                        (month-4)->{
                                            var izintemp = 0f
                                            var masuktemp = 0f
                                            for(presensi in agenda.children){
                                                if(presensi.value==0L){
                                                    izintemp+=1f
                                                }else{
                                                    masuktemp+=1f
                                                }
                                            }
                                            izin[1]+=(izintemp/total)*100
                                            masuk[1]+=(masuktemp/total)*100
                                            totalAgenda[1]+=1f
                                        }
                                        (month-3)->{
                                            var izintemp = 0f
                                            var masuktemp = 0f
                                            for(presensi in agenda.children){
                                                if(presensi.value==0L){
                                                    izintemp+=1f
                                                }else{
                                                    masuktemp+=1f
                                                }
                                            }
                                            izin[2]+=(izintemp/total)*100
                                            masuk[2]+=(masuktemp/total)*100
                                            totalAgenda[2]+=1f
                                        }
                                        (month-2)->{
                                            var izintemp = 0f
                                            var masuktemp = 0f
                                            for(presensi in agenda.children){
                                                if(presensi.value==0L){
                                                    izintemp+=1f
                                                }else{
                                                    masuktemp+=1f
                                                }
                                            }
                                            izin[3]+=(izintemp/total)*100
                                            masuk[3]+=(masuktemp/total)*100
                                            totalAgenda[3]+=1f
                                        }
                                        (month-1)->{
                                            var izintemp = 0f
                                            var masuktemp = 0f
                                            for(presensi in agenda.children){
                                                if(presensi.value==0L){
                                                    izintemp+=1f
                                                }else{
                                                    masuktemp+=1f
                                                }
                                            }
                                            izin[4]+=(izintemp/total)*100
                                            masuk[4]+=(masuktemp/total)*100
                                            totalAgenda[4]+=1f
                                        }
                                        (month)->{
                                            var izintemp = 0f
                                            var masuktemp = 0f
                                            for(presensi in agenda.children){
                                                if(presensi.value==0L){
                                                    Log.d("TAG", "SINI DONG")
                                                    izintemp+=1f
                                                }else{
                                                    Log.d("TAG", "LAH SANA")
                                                    masuktemp+=1f
                                                }
                                            }
                                            izin[5]+=(izintemp/total)*100
                                            masuk[5]+=(masuktemp/total)*100
                                            totalAgenda[5]+=1f
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
                for (i in 0..5){
                    if(totalAgenda[i]>0f){
                        izin[i]/=totalAgenda[i]
                        masuk[i]/=totalAgenda[i]
                    }
                }
                getAllPresensiListener.onSuccess(masuk,izin)
            }
            override fun onCancelled(error: DatabaseError) {
                getAllPresensiListener.onCancelled(error.message)
            }
        })
    }
    fun setOnAllPresensiListener(listener: GetPresensisListener) {
        getAllPresensiListener = listener
    }
}
