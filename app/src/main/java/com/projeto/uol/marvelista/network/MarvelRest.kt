package com.projeto.uol.marvelista.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object MarvelHandler {
  val MARVEL_BASE_URL = "https://gateway.marvel.com:443/v1/public/"
  val service: MarvelServiceInterface = Retrofit.Builder()
    .baseUrl(MARVEL_BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .client(getOkHttpClient())
    .build()
    .create(MarvelServiceInterface::class.java)

}

private fun getOkHttpClient(): OkHttpClient {
  val logging = HttpLoggingInterceptor()
  logging.level = HttpLoggingInterceptor.Level.BODY
  val builder = OkHttpClient.Builder()
  builder.addInterceptor(addKeyAndHash())
  builder.addInterceptor(logging)
  val okHttpClient = builder.build()
  return okHttpClient
}

fun addKeyAndHash(): Interceptor = Interceptor { chain ->
  var params = mapOf(
    "apikey" to "490e9405e90d6cdf7f823da1d81c7972",
    "hash" to "52e42a722bdb19550e304cd646af3fbc",
    "ts" to "1"
  )
  val originalRequest = chain.request()
  val urlWithParams = originalRequest.url().newBuilder()
    .apply { params.forEach { addQueryParameter(it.key, it.value) } }
    .build()
  val newRequest = originalRequest.newBuilder().url(urlWithParams).build()

  chain.proceed(newRequest)
}
