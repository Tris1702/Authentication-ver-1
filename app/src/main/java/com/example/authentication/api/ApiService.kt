package com.example.authentication.api

import com.example.authentication.model.User
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    companion object{
        var gson = GsonBuilder()
            .setDateFormat("yyyy-mm-dd HH:mm:ss")
            .create()
        var apiService: ApiService = Retrofit.Builder()
            .baseUrl("http://192.168.21.109:3000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }

    @GET("users")
    fun checkUser(@Query("userEmail") userName: String): Call<ArrayList<User> >

    @POST("users")
    fun addUser(@Body user: User): Call<User>

    @GET("users")
    fun getDataFromToken(@Query("token") userToken: String): Call<ArrayList<User> >
}