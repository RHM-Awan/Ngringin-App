package com.rahmawan.ngringinapk.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Agenda(
    var idAgenda:String?=null,
    var title:String?=null,
    var body:String?=null,
    var dateStart:Long?=null,
    var dateFinish:Long?=null,
    var address:String?=null,
    var latitude:Double?=null,
    var longtitude:Double?=null
): Parcelable
