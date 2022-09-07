package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.enumeration.AttachmentType


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
    var read: Boolean = true,
    @Embedded
    var attachment: AttachmentEmbedded?,


    ) {

    fun toDto() = Post(
        id = id,
        author = author,
        content = content,
        published = published,
        likedByMe = likedByMe,
        likes = likes,
        shareCount = shareCount,
        authorAvatar = authorAvatar,
        attachment = attachment?.let {
            Attachment(it.url, it.type)
        }
    )

    companion object {
        fun fromDto(post: Post): PostEntity =
            with(post) {
                PostEntity(id = id,
                    author = author,
                    content = content,
                    published = published,
                    likedByMe = likedByMe,
                    likes = likes,
                    shareCount = shareCount,
                    authorAvatar = authorAvatar,
                    attachment = attachment?.let {
                        AttachmentEmbedded(it.url, it.type)
                    }
                )
            }
    }

}

data class AttachmentEmbedded(
    var url: String,
    var type: AttachmentType,
)

fun Post.toPostEntity(): PostEntity =
    PostEntity(id = id,
        author = author,
        content = content,
        published = published,
        likedByMe = likedByMe,
        likes = likes,
        shareCount = shareCount,
        authorAvatar = authorAvatar,
        attachment = attachment?.let {
            AttachmentEmbedded(it.url, it.type)
        }
    )

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)