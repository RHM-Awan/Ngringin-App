package com.rahmawan.ngringinapk.firebase.galeri

import android.net.Uri
import com.rahmawan.ngringinapk.firebase.Firebase
import com.rahmawan.ngringinapk.model.Galeri
import com.rahmawan.ngringinapk.model.User

object EditGaleri {
    private lateinit var editGaleriListener: EditGaleriListener
    interface EditGaleriListener {
        fun onSuccess(result:String)
        fun onFailure(error:String)
    }
    fun editGaleri(galeri: Galeri, gambar1: Uri?, gambar2: Uri?, gambar3: Uri?){
        Firebase.galeriRef.child(galeri.idGaleri!!).setValue(galeri).addOnFailureListener {
            editGaleriListener.onFailure(it.toString())
        }.addOnSuccessListener {
            editGaleriListener.onSuccess("Edit Berhasil")
            gambar1?.let {
                Firebase.storage.child(galeri.idGaleri+"1").putFile(it)
                    .addOnSuccessListener{ taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {uri->
                            galeri.gambar1 = uri.toString()
                            Firebase.galeriRef.child(galeri.idGaleri!!).setValue(galeri).addOnFailureListener {ex->
                                editGaleriListener.onFailure(ex.toString())
                            }.addOnSuccessListener {
                                editGaleriListener.onSuccess("Gambar 1 Berhasil Diubah")
                            }
                        }
                    }.addOnFailureListener{ex->
                        editGaleriListener.onFailure(ex.toString())
                    }
            }
            gambar2?.let {
                Firebase.storage.child(galeri.idGaleri+"2").putFile(it)
                    .addOnSuccessListener{ taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {uri->
                            galeri.gambar2 = uri.toString()
                            Firebase.galeriRef.child(galeri.idGaleri!!).setValue(galeri).addOnFailureListener {ex->
                                editGaleriListener.onFailure(ex.toString())
                            }.addOnSuccessListener {
                                editGaleriListener.onSuccess("Gambar 2 Berhasil Diubah")
                            }
                        }
                    }.addOnFailureListener{ex->
                        editGaleriListener.onFailure(ex.toString())
                    }
            }
            gambar3?.let {
                Firebase.storage.child(galeri.idGaleri+"3").putFile(it)
                    .addOnSuccessListener{ taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {uri->
                            galeri.gambar3 = uri.toString()
                            Firebase.galeriRef.child(galeri.idGaleri!!).setValue(galeri).addOnFailureListener {ex->
                                editGaleriListener.onFailure(ex.toString())
                            }.addOnSuccessListener {
                                editGaleriListener.onSuccess("Gambar 3 Berhasil Diubah")
                            }
                        }
                    }.addOnFailureListener{ex->
                        editGaleriListener.onFailure(ex.toString())
                    }
            }
        }
    }
    fun setOnEditGaleriListener(listener: EditGaleriListener) {
        editGaleriListener = listener
    }
}