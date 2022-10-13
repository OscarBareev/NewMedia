package ru.netology.nmedia.adapter


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.transformCount


interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onCard(post: Post) {}
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Post, PostViewHolder>(PostDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}


class PostViewHolder(
    private val binding: PostCardBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {


            authorTxt.text = post.author
            publishedTxt.text = post.published
            contentTxt.text = post.content
            likesBtn.isChecked = post.likedByMe
            likesBtn.text = transformCount(post.likes)
            shareBtn.text = transformCount(post.shareCount)

            likesBtn.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            shareBtn.setOnClickListener {
                onInteractionListener.onShare(post)
            }


            if (post.attachment != null) {
                binding.group.visibility = VISIBLE

                val attUrl = "http://10.0.2.2:9999/media/${post.attachment.url}"
                Glide.with(binding.attImg)
                    .load(attUrl)
                    .timeout(10_000)
                    .into(binding.attImg)


                binding.attImg.setOnClickListener { view ->


                    view.findNavController()
                        .navigate(
                            R.id.action_feedFragment_to_onlyImageFragment,
                            Bundle().apply {
                                textArg = attUrl
                            }
                        )
                }

            } else {
                binding.group.visibility = GONE
            }


            itemView.setOnClickListener {
                onInteractionListener.onCard(post)
            }


            val avatarUrl = "http://10.0.2.2:9999/avatars/${post.authorAvatar}"
            Glide.with(binding.avatarImg)
                .load(avatarUrl)
                .placeholder(R.drawable.ic_baseline_android_24)
                .error(R.drawable.ic_baseline_error_24)
                .circleCrop()
                .timeout(10_000)
                .into(binding.avatarImg)


            menuBtn.visibility = if(post.ownedByMe) VISIBLE else INVISIBLE
            menuBtn.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }


                            else -> false
                        }
                    }
                }.show()

            }


        }
    }
}

class PostDiffCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}