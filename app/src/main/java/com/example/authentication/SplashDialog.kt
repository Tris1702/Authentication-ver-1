package com.example.authentication

import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity

class SplashDialog (private val activity: FragmentActivity){
    lateinit var dialog: AlertDialog
    fun startSplashDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity, R.style.DialogTheme)

        val inflater: LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.fragment_splash, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun closeSplashDialog(){
        dialog.dismiss()
    }
}