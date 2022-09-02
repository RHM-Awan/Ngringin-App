package com.rahmawan.ngringinapk

import android.content.Context
import android.graphics.drawable.InsetDrawable
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
//garis
object RecyclerUtils {
    val itemDecor:(ctx:Context)-> DividerItemDecoration ={
        val attrs = intArrayOf(android.R.attr.listDivider)
        val a = it.obtainStyledAttributes(attrs)
        val divider = a.getDrawable(0)
        val inset = it.resources.getDimensionPixelSize(R.dimen.margin_recyclerview)
        val insetDivider = InsetDrawable(divider, inset, 0, inset, 0)
        a.recycle()
        val itemDecoration = DividerItemDecoration(it, LinearLayoutManager.VERTICAL)
        itemDecoration.setDrawable(insetDivider)
        itemDecoration
    }
}