package com.example.authentication.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.authentication.LoadingDialog
import com.example.authentication.R
import com.example.authentication.databinding.FragmentSignUpBinding
import com.example.authentication.hashPassword.Md5
import com.example.authentication.api.ApiRequest
import com.example.authentication.model.User
import kotlinx.coroutines.*

class SignUp : Fragment() {
    lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        binding = FragmentSignUpBinding.bind(view)


        binding.btnSignUp.setOnClickListener{
            if (binding.edtName.text.isNotEmpty() && binding.edtEmail.text.isNotEmpty() && binding.edtPassword.text.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val apiRequest = ApiRequest()
                    apiRequest.updateUser(
                        User(
                            0,
                            binding.edtName.text.toString(),
                            binding.edtEmail.text.toString(),
                            Md5(binding.edtPassword.text.toString()).input,
                            ""
                        ), activity
                    ) {
                        launch(Dispatchers.Main) {
                            when (it) {
                                "0" //Tai khoan Email da duoc su dung
                                -> Toast.makeText(
                                    activity,
                                    "This email has been used",
                                    Toast.LENGTH_SHORT
                                ).show()
                                "1" -> Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                                else //Dang ki tai khoan thanh cong
                                -> {
                                    Toast.makeText(
                                        activity,
                                        "You're account has been created",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val sharePreferences = activity?.getSharedPreferences(
                                        "token",
                                        Context.MODE_PRIVATE
                                    )
                                    sharePreferences?.edit()?.putString("accessToken", it)?.apply()
                                    Log.e(
                                        "token moi",
                                        sharePreferences?.getString("accessToken", "none")!!
                                    )
                                    //Call loading dialog
                                    val loadingDialog = LoadingDialog(requireActivity())
                                    loadingDialog.startLoadingDialog()
                                    lifecycleScope.launch() {
                                        delay(3000)
                                        loadingDialog.closeLoadingDialog()
                                        Navigation.findNavController(view)
                                            .navigate(R.id.action_signUp_to_logOut)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else{
                Toast.makeText(activity, "Please fill all fields", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnSwitchToSignIn.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_signUp_to_signIn)
        }
        return view
    }

}