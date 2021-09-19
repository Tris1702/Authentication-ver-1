package com.example.authentication.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.authentication.hashPassword.Md5
import com.example.authentication.model.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiRequest{

    fun updateUser(newUser: User, fragment: FragmentActivity?, returnValue: (Int) -> Unit){ //nho phai truyen hashcode
        checkEmail(newUser.userEmail, fragment){
                if (!it){
                    //create Token from userName, userEmail and userPassword
                    val claims = HashMap<String, Any?>()
                    claims["userName"] = newUser.userName
                    claims["userEmail"] = newUser.userEmail
                    claims["userHashPassword"] = newUser.userHashPassword
                    val token = Jwts.builder().setClaims(claims)
                        .signWith(SignatureAlgorithm.HS256, "9CC746C50579A6DF1DF5B1595290B94A".toByteArray()).toString()
                    newUser.token = token //update token cho user
                    ApiService.apiService.addUser(newUser).enqueue(object: Callback<User>{
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            Log.e("check", token)
                            returnValue(1)//Dang ki tk thanh cong
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            returnValue(2) //Call Api loi
                        }
                    })
                }
                else{
                    returnValue(0) //Email da duoc su dung
                }
        }
    }

    fun checkUser(userEmail: String, userPassword: String, fragment: FragmentActivity?, returnValue: (Int) -> Unit){
        ApiService.apiService.checkUser(userEmail).enqueue(object: Callback<ArrayList<User> >{
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                val listUsers = response.body() as ArrayList<User>
                if (listUsers.isEmpty()) {
                    returnValue(0) //Khong tim thay tai khoan
                }
                else {
                    val tmpInput = Md5(userPassword).input
                    Log.e("check", tmpInput)
                    if (listUsers[0].userHashPassword.equals(tmpInput)){
                        Log.e("check", "successful")
                        Log.e("check", listUsers[0].userHashPassword)
                        Toast.makeText(fragment, "Sign in successful", Toast.LENGTH_SHORT).show()

                        //save token
                        fragment?.applicationContext?.let{
                            Log.e("check", "ok")
                            val sharePreferences = it.getSharedPreferences("token", Context.MODE_PRIVATE)
                            sharePreferences.edit().putString("accessToken", listUsers[0].token).apply()
                            returnValue(1)//Tai khoan ton tai, dang nhap thanh cong
                        }
                    }
                    else {
                        returnValue(2) //Tai khoan ton tai, sai mat khau
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                returnValue(3) //Loi Api
            }


        })
    }

    fun getData(token: String, returnValue: (Boolean, String) -> Unit){
        Log.e("checkk", token)
        ApiService.apiService.getDataFromToken(token).enqueue(object:
            Callback<ArrayList<User> > {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                val user = (response.body() as ArrayList)[0]
                Log.e("checkk", user.userName+"/"+user.userEmail)
                returnValue(true, user.userName) //Lay thanh cong
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                returnValue(false, "")
            }

        })
    }

    private fun checkEmail(userEmail: String, fragment: FragmentActivity?, returnValue: (Boolean) -> Unit){
        var result: Boolean
        ApiService.apiService.checkUser(userEmail).enqueue(object: Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                val validUsers = response.body() as ArrayList<User>
                result = validUsers.isNotEmpty()
                returnValue(result)
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Toast.makeText(fragment?.applicationContext, "Error", Toast.LENGTH_LONG).show()
            }
        })
    }
}