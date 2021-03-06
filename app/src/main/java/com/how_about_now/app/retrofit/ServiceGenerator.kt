package com.how_about_now.app.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit

/**
 * Created by User on 3/11/2017.
 */

object ServiceGenerator {
    val API_BASE_URL = "https://clientstagingdev.com/how_about_now/API/"
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS)


    private val builder = Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create())

    fun <S> createService(serviceClass: Class<S>): S {
        return createService(serviceClass, null)

    }

    fun <S> createService(serviceClass: Class<S>, authToken: String?): S {
        if (authToken != null) {
            httpClient.interceptors().add(Interceptor { chain ->
                val original = chain.request()

                // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                    .header("Authorization", authToken)
                    .method(original.method(), original.body())

                val request = requestBuilder.build()
                chain.proceed(request)
            })
        }
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = httpClient.build()
        httpClient.addInterceptor(interceptor)
        val retrofit = builder.client(client).build()

        return retrofit.create(serviceClass)
    }
}