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
    val likes: Int = 0,
    val shareCount: Int = 0,
    val authorAvatar: String,
    val video: String = "",

) {

    fun toDto() = Post(id, author, content, published, likedByMe, likes, shareCount, authorAvatar)

    companion object {
        fun fromDto(post: Post): PostEntity =
            with(post) {
                PostEntity(id, author, content, published, likedByMe, likes, shareCount, authorAvatar)
            }
    }

}

fun PostEntity.toPost(): Post =
    Post(id, author, content, published, likedByMe, likes, shareCount, authorAvatar, video)

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)