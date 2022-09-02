package com.rahmawan.ngringinapk.fragment.admin

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahmawan.ngringinapk.adapter.ListPresensiAdapter
import com.rahmawan.ngringinapk.databinding.FragmentListPresensiBinding
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.firebase.presensi.GetPresensis
import com.rahmawan.ngringinapk.firebase.user.GetUser
import com.rahmawan.ngringinapk.firebase.user.GetUsers
import com.rahmawan.ngringinapk.model.Agenda
import com.rahmawan.ngringinapk.model.Presensi
import com.rahmawan.ngringinapk.model.User
import kotlin.collections.ArrayList

class ListPresensiFragment(val agenda: Agenda) : Fragment() {

    private lateinit var bind: FragmentListPresensiBinding
    private val getPresensis=GetPresensis
    private val getUser=GetUser
    private var presensisUser:ArrayList<Presensi> = ArrayList()
    private var suggestUser:ArrayList<String> = ArrayList()
    private var users:ArrayList<User> = ArrayList()
    val getUsers = GetUsers

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        bind = FragmentListPresensiBinding.inflate(inflater,container,false)
        initListener()
        bind.rvPresensi.layoutManager= LinearLayoutManager(requireContext())
        getPresensis.getPresensis(agenda.idAgenda!!)
        getUsers.getUsers()
        return bind.root
    }
    private fun initListener(){
        getUsers.setOnUsersListener(object: GetUsers.GetUsersListener{
            override fun onSuccess(result: ArrayList<User>) {
                users=result
            }

            override fun onSuccess(aktif: ArrayList<User>, tidakAktif: ArrayList<User>) {}

            override fun onCancelled(message: String) {
                Toast.makeText(requireContext(),"Terjadi Kesalahan, Coba lagi",Toast.LENGTH_SHORT).show()
            }
        })
        getUser.setOnUserListener(object :GetUser.GetUserPresensiListener{
            override fun onSuccess(result: Presensi) {
                presensisUser.add(result)
                bind.rvPresensi.adapter=ListPresensiAdapter(presensisUser,agenda.dateStart!!,agenda.idAgenda!!)
                suggestUser.clear()
                for(user in users){
                    var isSame = false
                    for(userpres in presensisUser){
                        if(user.idUser==userpres.id){
                            isSame=true
                            break
                        }
                    }
                    if(!isSame){
                        suggestUser.add(user.name!!)
                    }
                }
                bind.etNamaCari.setAdapter(ArrayAdapter(requireContext(),
                    R.layout.simple_dropdown_item_1line, suggestUser))
            }
            override fun onFailure(error: String) {
                Toast.makeText(requireContext(),error,Toast.LENGTH_SHORT).show()
            }
        })
        getPresensis.setOnPresensisListener(object :GetPresensis.GetPresensisListener{
            override fun onSuccess(result: ArrayList<Presensi>) {
                presensisUser= ArrayList()
                if(result.size==0){
                    bind.rvPresensi.adapter=ListPresensiAdapter(presensisUser,agenda.dateStart!!,agenda.idAgenda!!)
                }else {
                    for (test in result) {
                        getUser.getUser(test)
                    }
                }
            }
            override fun onCancelled(message: String) {
                Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
            }
        })

        bind.btnTambah.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                for (user in users){
                    if(user.name == bind.etNamaCari.text.toString()){
                        for(userpres in presensisUser){
                            if(user.idUser==userpres.id){
                                Toast.makeText(requireContext(),"User Telah Ada Di Presensi",Toast.LENGTH_SHORT).show()
                                return
                            }
                        }
                        if(user.statusActive=="aktif"){
                            if((bind.root.findViewById(bind.rgKeterangan.checkedRadioButtonId) as RadioButton).text=="Masuk"){
                                Firebase.presensiUserRef(user.idUser!!,agenda.idAgenda!!).setValue(agenda.dateStart)
                            }else{
                                Firebase.presensiUserRef(user.idUser!!,agenda.idAgenda!!).setValue(0)
                            }
                            Toast.makeText(requireContext(),"User Telah Ditambahkan",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireContext(),"User Tidak Aktif",Toast.LENGTH_SHORT).show()
                            return
                        }
                    }
                }
 //               Toast.makeText(requireContext(),"User Tidak Ditemukan",Toast.LENGTH_SHORT).show()
            }
        })
    }

}
