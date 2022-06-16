package br.com.anderson.composefirstlook.data.remote.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Intercepts and adds Token or API Key
 */
class AuthInterceptor(private val apiKey: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val httpUrl = original.url.newBuilder()
            .addQueryParameter("appid", apiKey)
            .build()

        val requestBuilder: Request.Builder = original.newBuilder()
            .url(httpUrl)

        return chain.proceed(requestBuilder.build())
    }
}