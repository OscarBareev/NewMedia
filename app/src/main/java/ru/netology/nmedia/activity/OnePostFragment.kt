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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
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
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentOnePostBinding.inflate(layoutInflater, container, false)
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
        val postId: Long? = arguments?.longArg

        binding.onePostContainer.removeAllViews()
        PostCardBinding.inflate(layoutInflater, binding.onePostContainer, true).apply {

            viewModel.data.map { state ->
                state.posts.find { it.id == postId }
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
                likesBtn.text = transformCount(post.likes)
                shareBtn.text = transformCount(post.shareCount)


                likesBtn.setOnClickListener {
                    viewModel.like(post)
                    viewModel.loadPosts()
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


                if (post.attachment != null) {
                    group.visibility = View.VISIBLE

                    val attUrl = "http://10.0.2.2:9999/media/${post.attachment.url}"
                    Glide.with(attImg)
                        .load(attUrl)
                        .timeout(10_000)
                        .into(attImg)


                    attImg.setOnClickListener {view ->


                        view.findNavController()
                            .navigate(
                                R.id.action_onePostFragment_to_onlyImageFragment,
                                Bundle().apply {
                                    textArg = attUrl
                                }
                            )
                    }
                } else {
                    group.visibility = View.GONE
                }


                val avatarUrl = "http://10.0.2.2:9999/avatars/${post.authorAvatar}"
                Glide.with(avatarImg)
                    .load(avatarUrl)
                    .placeholder(R.drawable.ic_baseline_android_24)
                    .error(R.drawable.ic_baseline_error_24)
                    .circleCrop()
                    .timeout(10_000)
                    .into(avatarImg)


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