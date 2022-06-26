package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post


@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val likesCount: Int = 0,
    val shareCount: Int = 0,
    val video: String = ""
) {
    companion object {
        fun fromPost(post: Post): PostEntity =
            with(post) {
                PostEntity(id, author, content, published, likedByMe, likes, shareCount, video)
            }
    }

}

fun PostEntity.toPost(): Post =
    Post(id, author, content, published, likedByMe, likesCount, shareCount, video)