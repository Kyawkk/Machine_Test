package com.kyawzinlinn.machinetest.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kyawzinlinn.machinetest.databinding.ErrorDialogBinding

fun getImgIndex(url: String): String{
    val segments = url.split("/")
    return segments.get(segments.size - 2)
}

fun showErrorDialog(context: Context, title: String, desc: String, onRetry: () -> Unit){
    val dialogBuilder = MaterialAlertDialogBuilder(context)
    val dialogBinding = ErrorDialogBinding.inflate(LayoutInflater.from(context),null,false)
    dialogBuilder.setView(dialogBinding.root)

    val dialog = dialogBuilder.create()
    dialog.setCancelable(false)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    dialogBinding.apply {
        tvErrorTitle.text = title
        tvError.text = desc
        btnErrorAction.setOnClickListener {
            onRetry()
            dialog.dismiss()
        }
    }

    dialog.show()
}