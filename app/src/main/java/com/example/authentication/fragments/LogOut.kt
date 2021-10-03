package com.example.authentication.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.authentication.R
import com.example.authentication.databinding.FragmentLogOutBinding
import com.example.authentication.api.ApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogOut : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log_out, container, false)
        val binding = FragmentLogOutBinding.bind(view)
        context?.let{
            val sharesPreferences = it.getSharedPreferences("token", Context.MODE_PRIVATE)
            val token = sharesPreferences.getString("accessToken", "none")
            token?.let {
                lifecycleScope.launch(Dispatchers.IO) {
                    val apiRequest = ApiRequest()
                    apiRequest.getData(token) { x, userName ->
                        run {
                            launch(Dispatchers.Main) {
                                if (x) {
                                    binding.tvName.text =
                                        ("${resources.getString(R.string.hello)}\n$userName")
                                } else
                                    Toast.makeText(activity, "Wrong Token", Toast.LENGTH_SHORT)
                                        .show()
                            }
                        }
                    }
                }
            }
            binding.btnLogOut.setOnClickListener {
                    sharesPreferences.edit().putString("accessToken", "none").apply()
                    Navigation.findNavController(view).navigate(R.id.action_logOut_to_signIn)
                }
        }
        return view
    }

}