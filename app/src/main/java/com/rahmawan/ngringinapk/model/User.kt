package com.rahmawan.ngringinapk.model
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
@Parcelize
data class User(
    var idUser:String?=null,
    var name:String?=null,
    var email:String?=null,
    var password:String?=null,
    var birthday:Long?=-1,
    var phone:String?=null,
    var gender:String?=null,
    var image:String?=null,
    var typeAccount:String?=null,
    var statusActive:String?=null,
    var address:String?=null
):Parcelable
