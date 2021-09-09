package com.example.authentication.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.authentication.R
import com.example.authentication.api.ApiService
import com.example.authentication.databinding.FragmentLogOutBinding
import com.example.authentication.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                Log.e("checkk", token)
                ApiService.apiService.getDataFromToken(token).enqueue(object:
                    Callback<ArrayList<User> > {
                    override fun onResponse(
                        call: Call<ArrayList<User>>,
                        response: Response<ArrayList<User>>
                    ) {
                        val user = (response.body() as ArrayList)[0]
                        Log.e("checkk", user.userName+"/"+user.userEmail)
                        binding.tvName.text = user.userName
                        binding.tvEmail.text = user.userEmail
                    }

                    override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                        Toast.makeText(activity, "Wrong Token", Toast.LENGTH_SHORT).show()
                    }

                })
            }
            binding.btnLogOut.setOnClickListener {
                    sharesPreferences.edit().putString("accessToken", "none").apply()
                    Navigation.findNavController(view).navigate(R.id.action_logOut_to_signIn)
                }
        }
        return view
    }

}