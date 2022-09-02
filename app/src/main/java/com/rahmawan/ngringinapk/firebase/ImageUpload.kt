package com.rahmawan.ngringinapk.firebase

import android.net.Uri


object ImageUpload {
    private lateinit var uploadImageListener: UploadImageListener
    interface UploadImageListener {
        fun onSuccess(result:String)
        fun onFailure(error:String)
    }
    fun uploadImage(file: Uri, id:String){
        Firebase.storage.child(id).putFile(file)
            .addOnSuccessListener{ taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    uploadImageListener.onSuccess(it.toString())
                }
            }
            .addOnFailureListener{
                uploadImageListener.onFailure(it.toString())
            }
    }
    fun setOnUploadImageListener(listener: UploadImageListener) {
        uploadImageListener = listener
    }
}