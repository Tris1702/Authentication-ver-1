package com.example.authentication.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.authentication.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        lifecycleScope.launch{
            delay(3000)
            val sharesPreferences = context?.getSharedPreferences("token", Context.MODE_PRIVATE)
            val token = sharesPreferences?.getString("accessToken", "none")
            if (token.equals("none")) //neu may khong luu token thi chuyen den signIn
            {
                Navigation.findNavController(view)
                    .navigate(R.id.action_splashFragment_to_signIn)
            }
            else {
                //neu co thi chuyen den logout
                Navigation.findNavController(view)
                    .navigate(R.id.action_splashFragment_to_logOut)
            }
        }
        return view
    }
}