package com.prederikochan.weatherapp.data.network

import android.content.Context
import android.os.Build
import androidx.annotation.Nullable
import com.google.gson.Gson
import com.prederikochan.weatherapp.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkClient {
    companion object {
        fun <T> create(
            service: Class<T>
        ): T {

            val gson = Gson()
            gson.excluder().excludeFieldsWithoutExposeAnnotation()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(createClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
            return retrofit.create(service)
        }

        private fun createClient(): OkHttpClient {
            val connectTimeOut: Long = 20
            val readTimeOut: Long = 20
            val writeTimeOut: Long = 20
            val callTimeOut: Long = 20

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val builder = OkHttpClient.Builder()
                .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.SECONDS)
                .callTimeout(callTimeOut, TimeUnit.SECONDS)
                .addInterceptor(InternalHTTPInterceptor())

            if (BuildConfig.DEBUG) {
                builder.addInterceptor(loggingInterceptor)
            }

            return builder.build()
        }

    }
}

class InternalHTTPInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val originalUrl = original.url
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("appid", BuildConfig.WEATHER_API)
            .build()

        val newRequest = original.newBuilder().url(newUrl).build()
        val response = chain.proceed(newRequest)

        return response.newBuilder().build()
    }
}