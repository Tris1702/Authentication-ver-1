package com.example.authentication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val sharesPreferences = applicationContext.getSharedPreferences("token", Context.MODE_PRIVATE)
        val token = sharesPreferences.getString("accessToken", "none")
        if (token.equals("none"))
        {
            navController.navigate(R.id.signIn)
        }
        else {
            navController.navigate(R.id.logOut)
        }
    }
}