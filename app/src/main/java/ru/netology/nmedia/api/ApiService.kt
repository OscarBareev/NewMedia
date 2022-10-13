package ru.netology.nmedia.api

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:9999/api/slow/"

private val okHttpClient = OkHttpClient.Builder()
    .let {
        if (BuildConfig.DEBUG) {
            it.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        } else {
            it
        }
    }
    .addInterceptor { chain ->
        AppAuth.getInstance().authStateFlow.value.token?.let { token ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", token)
                .build()
            return@addInterceptor chain.proceed(newRequest)
        }
        chain.proceed(chain.request())

    }

    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .build()

interface ApiService {

    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getById(@Path("id") id: Long): Response<Post>

    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    @POST("posts/{id}/likes")
    suspend fun like(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun unLike(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{postId}")
    suspend fun deleteById(@Path("postId") postId: Long): Response<Unit>

    @Multipart
    @POST("media")
    suspend fun upload(@Part media: MultipartBody.Part): Response<Media>

}

object ApiServiceHolder {
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}