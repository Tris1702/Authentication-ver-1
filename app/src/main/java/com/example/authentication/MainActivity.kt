package com.example.authentication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val splashDialog = SplashDialog(this)
        splashDialog.startSplashDialog()
        lifecycleScope.launch{
            delay(3000)
            splashDialog.closeSplashDialog()
            doAfter(supportFragmentManager, applicationContext)
        }
    }
}

fun doAfter( supportFragmentManager: FragmentManager, context: Context){
    val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
    val navController = navHostFragment.navController

    val sharesPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)
    val token = sharesPreferences?.getString("accessToken", "none")
    if (token.equals("none")) //neu may khong luu token thi chuyen den signIn
    {
        navController.navigate(R.id.signIn)
    }
    else {
        //neu co thi chuyen den logout
        navController.navigate(R.id.logOut)
    }
}