package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.databinding.FragmentOnlyImageBinding
import ru.netology.nmedia.dto.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel


class OnlyImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentOnlyImageBinding.inflate(
            inflater,
            container,
            false
        )

        val attUrl = arguments?.textArg


        Glide.with(binding.photo)
            .load(attUrl)
            .timeout(10_000)
            .error(R.drawable.ic_baseline_error_24)
            .into(binding.photo)




        return binding.root
    }


}