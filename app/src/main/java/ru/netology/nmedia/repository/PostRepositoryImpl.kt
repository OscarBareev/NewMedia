package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl : PostRepository {
    var post = Post(
        id = 1,
        author = "Нетология. Университет интернет профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике, и управлению. Мы растем сами и помогаем расти студентам: от новичков, до уверенных профессионалов. Но самое важное остается с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен -> http://netolo.gy/fyb.",
        published = "21 мая в 18:36",
        likedByMe = false,
        likesCount = 1099,
        shareCount = 999_999
    )

    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {
        val count = if (post.likedByMe) post.likesCount - 1 else post.likesCount + 1
        post = post.copy(likedByMe = !post.likedByMe, likesCount = count)
        data.value = post
    }

    override fun share() {
        val count = post.likesCount + 1
        post = post.copy(shareCount = count)
        data.value = post
    }
}