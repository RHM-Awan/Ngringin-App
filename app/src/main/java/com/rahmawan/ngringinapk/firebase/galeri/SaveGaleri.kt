package com.rahmawan.ngringinapk.firebase.galeri

import android.net.Uri
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Galeri
import java.util.*

object SaveGaleri {
    private lateinit var saveGaleriListener: SaveGaleriListener
    interface SaveGaleriListener {
        fun onSuccess(result:String)
        fun onFailure(error:String)
    }
    fun saveGaleri(judul:String, deskripsi:String, gambar1: Uri?, gambar2:Uri?, gambar3:Uri?, uid:String){
        val idGaleri: String? = Firebase.galeriRef.push().key
        val galeri = Galeri(idGaleri,judul,deskripsi,null,null,null,uid, Calendar.getInstance().time.time)
        if(idGaleri!=null){
            Firebase.galeriRef.child(idGaleri).setValue(galeri).addOnFailureListener {
                saveGaleriListener.onFailure(it.toString())
            }.addOnSuccessListener {
                saveGaleriListener.onSuccess("Menyimpan Galeri Berhasil!")
                gambar1?.let {
                    Firebase.storage.child(idGaleri+"1").putFile(it)
                        .addOnSuccessListener{ taskSnapshot ->
                            taskSnapshot.storage.downloadUrl.addOnSuccessListener {uri->
                                galeri.gambar1 = uri.toString()
                                Firebase.galeriRef.child(idGaleri).setValue(galeri).addOnFailureListener {ex->
                                    saveGaleriListener.onFailure(ex.toString())
                                }.addOnSuccessListener {
                                    saveGaleriListener.onSuccess("Gambar 1 Berhasil Ditambahkan")
                                }
                            }
                        }.addOnFailureListener{ex->
                            saveGaleriListener.onFailure(ex.toString())
                        }
                }
                gambar2?.let {
                    Firebase.storage.child(idGaleri+"2").putFile(it)
                        .addOnSuccessListener{ taskSnapshot ->
                            taskSnapshot.storage.downloadUrl.addOnSuccessListener {uri->
                                galeri.gambar2 = uri.toString()
                                Firebase.galeriRef.child(idGaleri).setValue(galeri).addOnFailureListener {ex->
                                    saveGaleriListener.onFailure(ex.toString())
                                }.addOnSuccessListener {
                                    saveGaleriListener.onSuccess("Gambar 2 Berhasil Ditambahkan")
                                }
                            }
                        }.addOnFailureListener{ex->
                            saveGaleriListener.onFailure(ex.toString())
                        }
                }
                gambar3?.let {
                    Firebase.storage.child(idGaleri+"3").putFile(it)
                        .addOnSuccessListener{ taskSnapshot ->
                            taskSnapshot.storage.downloadUrl.addOnSuccessListener {uri->
                                galeri.gambar3 = uri.toString()
                                Firebase.galeriRef.child(idGaleri).setValue(galeri).addOnFailureListener {ex->
                                    saveGaleriListener.onFailure(ex.toString())
                                }.addOnSuccessListener {
                                    saveGaleriListener.onSuccess("Gambar 3 Berhasil Ditambahkan")
                                }
                            }
                        }.addOnFailureListener{ex->
                            saveGaleriListener.onFailure(ex.toString())
                        }
                }
            }
        }
    }
    fun setOnSaveGaleriListener(listener: SaveGaleriListener) {
        saveGaleriListener = listener
    }
}