package com.example.plantdiarybyemdad

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private var retrofit: Retrofit? = null
    private var BASE_URL = "https://www.plantplaces.com"

    var retrofitInstance: Retrofit? = null
        get() {
            if (retrofit == null) {
                retrofit =
                    retrofit2.Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit
        }
}
