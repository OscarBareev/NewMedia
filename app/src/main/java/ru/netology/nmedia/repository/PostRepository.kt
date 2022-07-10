package ru.netology.nmedia.repository

import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun getAllAsync(callback: Callback<List<Post>>)
    fun likeAsync(post: Post, callback: Callback<Unit>)
    fun shareById(id: Long)
    fun removeByIdAsync(id: Long, callback: Callback<Unit>)
    fun saveAsync(post: Post, callback: Callback<Unit>)


    interface Callback<T> {
        fun onSuccess(par: T) {}
        fun onError(e: Exception) {}
    }
}