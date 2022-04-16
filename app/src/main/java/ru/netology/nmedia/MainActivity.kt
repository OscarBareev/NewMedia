package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.transformCount

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике, и управлению. Мы растем сами и помогаем расти студентам: от новичков, до уверенных профессионалов. Но самое важное остается с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен -> http://netolo.gy/fyb.",
            published = "21 мая в 18:36",
            likedByMe = false,
            likesCount = 1099,
            shareCount = 999_999
        )



        with(binding) {
            authorTxt.text = post.author
            publishedTxt.text = post.published
            contentTxt.text = post.content
            if (post.likedByMe) {
                likesImg.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
            likesTxt.text = transformCount(post.likesCount)

            if (post.shareCount > 0) shareTxt.text =
                transformCount(post.shareCount) else shareTxt.text =
                ""
        }


        binding.likesImg.setOnClickListener {

            if (post.likedByMe) {
                post.likedByMe = !post.likedByMe
                post.likesCount--
                binding.likesTxt.text = transformCount(post.likesCount)
                binding.likesImg.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            } else {
                post.likedByMe = !post.likedByMe
                post.likesCount++
                binding.likesTxt.text = transformCount(post.likesCount)
                binding.likesImg.setImageResource(R.drawable.ic_baseline_favorite_24)
            }

        }

        binding.shareImg.setOnClickListener {
            post.shareCount++
            binding.shareTxt.text = transformCount(post.shareCount)
        }
    }
}