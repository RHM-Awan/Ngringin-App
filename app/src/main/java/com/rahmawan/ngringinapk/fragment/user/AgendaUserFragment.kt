package com.rahmawan.ngringinapk.fragment.user


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahmawan.ngringinapk.RecyclerUtils
import com.rahmawan.ngringinapk.activity.admin.CreateAgendaActivity
import com.rahmawan.ngringinapk.activity.service.AgendaAlarmReceiver
import com.rahmawan.ngringinapk.adapter.AgendaAdapter
import com.rahmawan.ngringinapk.databinding.FragmentAgendaAdminBinding
import com.rahmawan.ngringinapk.databinding.FragmentAgendaUserBinding
import com.rahmawan.ngringinapk.firebase.agenda.GetAgendas
import com.rahmawan.ngringinapk.model.Agenda
import java.util.*
import kotlin.collections.ArrayList

class AgendaUserFragment : Fragment() {
    private lateinit var bind: FragmentAgendaUserBinding
    val getAgenda = GetAgendas
    private lateinit var alarmManager:AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var i :Intent
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentAgendaUserBinding.inflate(inflater,container,false)
        initListener()
        bind.rvAgenda.addItemDecoration(RecyclerUtils.itemDecor(requireContext()))
        bind.rvAgenda.layoutManager = LinearLayoutManager(requireContext())
        alarmManager = requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        i = Intent(requireContext(), AgendaAlarmReceiver::class.java)
        getAgenda.getAgendas()
        return bind.root
    }

    private fun initListener(){
        getAgenda.setOnAgendasListener(object : GetAgendas.GetAgendasListener{
            override fun onSuccess(result: ArrayList<Agenda>) {
                val a = result.sortByDescending {
                    it.dateStart
                }
                bind.rvAgenda.adapter = AgendaAdapter(result, "user")
                var cal  = Calendar.getInstance()
                for(res in result){
                    val cur = Calendar.getInstance()
                    cal.time = Date(res.dateStart!!)
                    cal.add(Calendar.HOUR, -1) // i jam sebelum waktu mulai.
                    if(cur.time<=cal.time){
                        pendingIntent = PendingIntent.getBroadcast(requireContext(), res.dateStart!!.toInt(), i.putExtra("id",res.idAgenda).putExtra("title",res.title),
                            PendingIntent.FLAG_UPDATE_CURRENT)
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent)
                    }else{
                        if( (PendingIntent.getBroadcast(requireContext(), res.dateStart!!.toInt(),
                                i,
                                PendingIntent.FLAG_NO_CREATE) != null)){
                            PendingIntent.getBroadcast(requireContext(), res.dateStart!!.toInt(),
                                i,
                                PendingIntent.FLAG_NO_CREATE).cancel()
                        }
                    }
                }
                return a
            }
            override fun onCancelled(message: String) {
                Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}