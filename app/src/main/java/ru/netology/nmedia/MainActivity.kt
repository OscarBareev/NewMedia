package ru.netology.nmedia

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewModel: PostViewModel by viewModels()


        val adapter = PostAdapter(object : OnInteractionListener {


            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }
        })


        binding.listRV.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0L) {
                return@observe
            }
            with(binding.contentEdTxt) {
                requestFocus()
                setText(post.content)
            }
            binding.group.visibility = View.VISIBLE

        }


        binding.cancelBtn.setOnClickListener {
            with(binding.contentEdTxt) {
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                if (binding.group.visibility == View.VISIBLE) {
                    binding.group.visibility = View.GONE
                }
            }
        }


        binding.saveBtn.setOnClickListener {
            with(binding.contentEdTxt) {
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.empty_content,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                if (binding.group.visibility == View.VISIBLE) {
                    binding.group.visibility = View.GONE
                }
            }
        }


    }
}