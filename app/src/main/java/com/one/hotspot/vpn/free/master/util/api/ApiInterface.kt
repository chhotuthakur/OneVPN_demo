package com.one.hotspot.vpn.free.master.util.api

import com.one.hotspot.vpn.free.master.util.api.data.IpLookup
import com.one.hotspot.vpn.free.master.util.api.data.Movie
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiInterface {
    @Headers("Content-Type: application/json")
    @GET("{ip}")
    fun getIpDetails(@Path(value = "ip") ip: String?): Call<IpLookup>

    @GET("volley_array.json")
    fun getMovies() : Call<List<Movie>>

    companion object {
        private var BASE_URL = "http://velmm.com/apis/"
        private var IP_BASE_URL = "http://ip-api.com/json/"
        fun createIp(): ApiInterface {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(IP_BASE_URL)
                    .build()
            return retrofit.create(ApiInterface::class.java)
        }
        fun create(): ApiInterface {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}