package com.example.authentication

import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity

class LoadingDialog (val activity: FragmentActivity){
    lateinit var dialog: AlertDialog
    fun startLoadingDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)

        val inflater: LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.fragment_loading, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun closeLoadingDialog(){
        dialog.dismiss()
    }
}