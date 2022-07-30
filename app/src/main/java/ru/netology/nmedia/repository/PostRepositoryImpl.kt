package ru.netology.nmedia.repository


import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.ApiServiceHolder
import ru.netology.nmedia.dto.Post
import java.io.IOException


class PostRepositoryImpl : PostRepository {


    override fun getAll(): List<Post> {

        return ApiServiceHolder.api.getAll()
            .execute()
            .let {
                if (it.isSuccessful) {
                    it.body() ?: error("Body is null")
                } else {
                    throw RuntimeException(it.message())
                }
            }

    }

    override fun getAllAsync(callback: PostRepository.Callback<List<Post>>) {
        ApiServiceHolder.api.getAll()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {


                    if (!response.isSuccessful){
                        callback.onError(RuntimeException(response.code().toString()))
                    }



                    val body = response.body() ?: run {
                        callback.onError(RuntimeException("Body is null"))
                        return
                    }

                    callback.onSuccess(body)

                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }


            })
    }


    override fun likeAsync(post: Post, callback: PostRepository.Callback<Unit>) {
        if (!post.likedByMe) {
            ApiServiceHolder.api.like(post.id)
                .enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {

                        if (!response.isSuccessful){
                            callback.onError(RuntimeException(response.code().toString()))
                        }

                        callback.onSuccess(Unit)
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(RuntimeException(t))
                    }
                })

        } else {
            ApiServiceHolder.api.unLike(post.id)
                .enqueue(object : Callback<Post> {
                    override fun onResponse(call: Call<Post>, response: Response<Post>) {

                        if (!response.isSuccessful){
                            callback.onError(RuntimeException(response.code().toString()))
                        }
                        callback.onSuccess(Unit)
                    }

                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        callback.onError(RuntimeException(t))
                    }
                })
        }
    }


    override fun shareById(id: Long) {
        // TODO("Not yet implemented")
    }


    override fun saveAsync(post: Post, callback: PostRepository.Callback<Unit>) {


        ApiServiceHolder.api.save(post)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {

                    if (!response.isSuccessful){
                        callback.onError(RuntimeException(response.code().toString()))
                    }

                    callback.onSuccess(Unit)
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }

            })

    }


    override fun removeByIdAsync(id: Long, callback: PostRepository.Callback<Unit>) {

        ApiServiceHolder.api.deleteById(id)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                    if (!response.isSuccessful){
                        callback.onError(RuntimeException(response.code().toString()))
                    }

                    callback.onSuccess(Unit)
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }

            })
    }
}