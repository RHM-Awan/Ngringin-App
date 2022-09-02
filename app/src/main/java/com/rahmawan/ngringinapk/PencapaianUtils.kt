package com.rahmawan.ngringinapk

import com.rahmawan.ngringinapk.model.Pencapaian

// hal x 100 / jumlah user (laki2) = untuk laki2 peresentase.

object PencapaianUtils {
    fun percentageAlquran(alqurans:ArrayList<Pencapaian>):Int{
        var total = 0
        for (alquran in alqurans){
            total+= alquran.hal!!
        }
        return total*100/600
    }
    fun percentageIqro(iqros:ArrayList<Pencapaian>):Int{
        var total = 0
        for (iqro in iqros){
            total+=iqro.hal!!
        }
        return total*100/196
    }
}