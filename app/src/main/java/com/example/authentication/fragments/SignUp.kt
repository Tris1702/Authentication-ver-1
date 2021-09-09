package com.example.authentication.fragments

import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.authentication.R
import com.example.authentication.api.ApiService
import com.example.authentication.databinding.FragmentSignUpBinding
import com.example.authentication.hashPassword.Md5
import com.example.authentication.model.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUp : Fragment() {
    lateinit var binding: FragmentSignUpBinding
    lateinit var listUsers: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        binding = FragmentSignUpBinding.bind(view)


        binding.btnSignUp.setOnClickListener{
            if (binding.edtName.text.isNotEmpty() && binding.edtEmail.text.isNotEmpty() && binding.edtPassword.text.isNotEmpty()) {
                checkIfExist(binding.edtEmail.text.toString()) {
                    if(!it) {
                        //create Token from userName, userEmail and userPassword
                        val claims = HashMap<String, Any?>()
                        claims["userName"] = binding.edtName.text.toString()
                        claims["userEmail"] = binding.edtEmail.text.toString()
                        claims["userHashPassword"] = Md5(binding.edtPassword.text.toString()).input
                        val token = Jwts.builder().setClaims(claims)
                            .signWith(SignatureAlgorithm.HS256, "9CC746C50579A6DF1DF5B1595290B94A".toByteArray()).toString()

                        val newUser = User(0, binding.edtName.text.toString(), binding.edtEmail.text.toString(), Md5(binding.edtPassword.text.toString()).input,token)
                        ApiService.apiService.addUser(newUser).enqueue(object: Callback<User>{
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                Log.e("check", token)
                                Toast.makeText(activity, "You're account has been created", Toast.LENGTH_SHORT).show()

                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                            }

                        })
                    }
                    else{
                        Toast.makeText(activity, "This email has been used", Toast.LENGTH_SHORT).show()
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
    private fun checkIfExist(userEmail: String, returnValue: (Boolean) -> Unit){
        var result: Boolean
        ApiService.apiService.checkUser(userEmail).enqueue(object: Callback<ArrayList<User> >{
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                listUsers = response.body() as ArrayList<User>
                result = listUsers.isNotEmpty()
                returnValue(result)
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show()
            }
        })
    }

}