package com.invincible.miniproject

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("response")
    fun getLatestEntry(): Call<ResponseDataClass>

}