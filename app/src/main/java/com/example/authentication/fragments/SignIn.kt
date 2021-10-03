package com.example.authentication.fragments

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
import com.example.authentication.databinding.FragmentSignInBinding
import com.example.authentication.api.ApiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignIn : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        val binding = FragmentSignInBinding.bind(view)


        binding.btnSignIn.setOnClickListener{
            if (binding.edtUserEmail.text.isNotEmpty() && binding.edtUserPassword.text.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val apiRequest = ApiRequest()
                    apiRequest.checkUser(
                        binding.edtUserEmail.text.toString(),
                        binding.edtUserPassword.text.toString(),
                        activity
                    ) {
//                        launch(Dispatchers.Main) {
                            when (it) {
                                0 //Tai khoan khong ton tai
                                -> Toast.makeText(
                                    activity,
                                    "Your email hasn't been created yet. Please sign up",
                                    Toast.LENGTH_SHORT
                                ).show()
                                1 // Tai khoan ton tai, dang nhap thanh cong
                                -> {
                                    //Call loading dialog
                                    val loadingDialog = LoadingDialog(requireActivity())
                                    loadingDialog.startLoadingDialog()
                                    lifecycleScope.launch() {
                                        delay(3000)
                                        loadingDialog.closeLoadingDialog()
                                        //chuyen fragment
                                        Navigation.findNavController(view)
                                            .navigate(R.id.action_signIn_to_logOut)
                                    }
                                }
                                2//Tai khoan ton tai, sai mat khau
                                -> Toast.makeText(activity, "wrong password", Toast.LENGTH_SHORT)
                                    .show()
                                else -> Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
                            }
//                        }
                    }
                }
            }
            else{
                Toast.makeText(activity, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSwitchToSignUp.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_signIn_to_signUp)
        }
//        binding.imgBtnLanguage.setOnClickListener{
//            if (resources.getIdentifier(binding.imgBtnLanguage.drawable.toString(),"drawable", activity?.packageName) == R.drawable.ic_united_kingdom){
//                Log.e("lang", "eng->vn")
//                binding.imgBtnLanguage.setImageResource(R.drawable.ic_vietnam)
//            }
//            else {
//                Log.e("lang", "vn->eng")
//                binding.imgBtnLanguage.setImageResource(R.drawable.ic_united_kingdom)
//            }
//        }
        return view
    }
}