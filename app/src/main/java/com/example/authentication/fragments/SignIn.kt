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
import com.example.authentication.databinding.FragmentSignInBinding
import com.example.authentication.hashPassword.Md5
import com.example.authentication.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignIn : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        val binding = FragmentSignInBinding.bind(view)


        binding.btnSignIn.setOnClickListener{
            if (binding.edtUserEmail.text.isNotEmpty() && binding.edtUserPassword.text.isNotEmpty()){
                ApiService.apiService.checkUser(binding.edtUserEmail.text.toString()).enqueue(object: Callback<ArrayList<User> >{
                    override fun onResponse(
                        call: Call<ArrayList<User>>,
                        response: Response<ArrayList<User>>
                    ) {
                        var listUsers = response.body() as ArrayList<User>
                        if (listUsers.isEmpty()) {
                            Toast.makeText(activity, "Your email hasn't been created yet. Please sign up", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            var tmpInput = Md5(binding.edtUserPassword.text.toString()).input
                            Log.e("check", tmpInput)
                            if (listUsers[0].userHashPassword.equals(tmpInput)){
                                Log.e("check", "successful")
                                Log.e("check", listUsers[0].userHashPassword)
                                Toast.makeText(activity, "Sign in successful", Toast.LENGTH_SHORT).show()

                                //save token
                                context?.let{
                                    Log.e("check", "ok")
                                    val sharePreferences = it.getSharedPreferences("token", Context.MODE_PRIVATE)
                                    sharePreferences.edit().putString("accessToken", listUsers[0].token).apply()
                                    Navigation.findNavController(view).navigate(R.id.action_signIn_to_logOut)
                                }
                            }
                            else {
                                Toast.makeText(activity, "wrong password", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                        Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                    }


                })
            }
        }

        binding.btnSwitchToSignUp.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_signIn_to_signUp)
        }
        return view
    }

}