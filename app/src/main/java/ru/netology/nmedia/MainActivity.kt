package ru.netology.nmedia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewModel: PostViewModel by viewModels()


        val adapter = PostAdapter({
            viewModel.likeById(it.id)
        }, {
            viewModel.shareById(it.id)
        })


        binding.listRV.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
    }
}