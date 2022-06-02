package ru.netology.nmedia.repository

import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import androidx.lifecycle.Transformations
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryImpl(
    private val dao: PostDao,
) : PostRepository {

    override fun getAll() = Transformations.map(dao.getAll()) { list ->
        list.map {
            Post(
                it.id,
                it.author,
                it.content,
                it.published,
                it.likedByMe,
                it.likesCount,
                it.shareCount,
                it.video
            )
        }
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromPost(post))
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}