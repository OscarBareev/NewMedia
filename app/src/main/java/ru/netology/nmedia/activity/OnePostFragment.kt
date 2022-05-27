package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentOnePostBinding
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.LongArg
import ru.netology.nmedia.dto.transformCount
import ru.netology.nmedia.viewmodel.PostViewModel


class OnePostFragment : Fragment() {
    companion object {
        var Bundle.longArg: Long? by LongArg
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentOnePostBinding.inflate(layoutInflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val postId: Long? = arguments?.longArg

        binding.onePostContainer.removeAllViews()
        PostCardBinding.inflate(layoutInflater, binding.onePostContainer, true).apply {
            viewModel.data.map { posts ->
                posts.find { it.id == postId }
            }.observe(viewLifecycleOwner) { post ->
                post ?: run {
                    // Пост удалили
                    findNavController().navigateUp()
                    return@observe
                }

                authorTxt.text = post.author
                publishedTxt.text = post.published
                contentTxt.text = post.content
                likesBtn.isChecked = post.likedByMe
                likesBtn.text = transformCount(post.likesCount)
                shareBtn.text = transformCount(post.shareCount)

                if (post.video.trim().isNotBlank()) {
                    group.visibility = View.VISIBLE
                }

                likesBtn.setOnClickListener {
                    viewModel.likeById(post.id)
                }

                shareBtn.setOnClickListener {
                    Intent(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, post.content)
                        .setType("text/plain")
                        .also {
                            Intent.createChooser(it, "Share with").also(::startActivity)
                            viewModel.shareById(post.id)
                        }
                }

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video.trim()))
                val shareIntent = Intent.createChooser(intent, "App for video")

                videoImg.setOnClickListener {
                    it.context.startActivity(shareIntent)
                }

                playBtn.setOnClickListener {
                    it.context.startActivity(shareIntent)
                }

                menuBtn.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {

                                R.id.remove -> {
                                    viewModel.removeById(post.id)
                                    findNavController().navigateUp()
                                    true
                                }

                                R.id.edit -> {
                                    viewModel.edit(post)
                                    findNavController()
                                        .navigate(
                                            R.id.action_onePostFragment_to_newPostFragment,
                                            Bundle().apply {
                                                textArg = post.content
                                            }
                                        )
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }
            }
        }

        return binding.root
    }
}