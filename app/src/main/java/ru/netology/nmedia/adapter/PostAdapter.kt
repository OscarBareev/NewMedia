package ru.netology.nmedia.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostCardBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.transformCount

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onShare(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onCard(post: Post){}
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener
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
    private val onInteractionListener: OnInteractionListener
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



            if (post.video?.trim()?.isBlank() == true) {
                binding.group.visibility = VISIBLE

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video.trim()))
                val shareIntent = Intent.createChooser(intent, "App for video")

                videoImg.setOnClickListener {
                    it.context.startActivity(shareIntent)
                }

                playBtn.setOnClickListener {
                    it.context.startActivity(shareIntent)
                }
            } else {
                binding.group.visibility = GONE
            }

            itemView.setOnClickListener {
                onInteractionListener.onCard(post)
            }







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