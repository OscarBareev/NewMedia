package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.activity.viewModels
import ru.netology.nmedia.dto.transformCount

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                authorTxt.text = post.author
                publishedTxt.text = post.published
                contentTxt.text = post.content
                likesTxt.text = transformCount(post.likesCount)
                shareTxt.text = transformCount(post.shareCount)

                likesImg.setImageResource(
                    if (post.likedByMe) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                )

            }
        }


        binding.likesImg.setOnClickListener {
            viewModel.like()
        }

        binding.shareImg.setOnClickListener {
            viewModel.share()
        }

    }
}