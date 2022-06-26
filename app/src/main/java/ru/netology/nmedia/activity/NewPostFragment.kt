package ru.netology.nmedia.activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.dto.StringArg
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    companion object {

        var Bundle.textArg: String? by StringArg
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)



        arguments?.textArg
            ?.let(binding.edit::setText)

        binding.edit.requestFocus()

        binding.ok.setOnClickListener {
            viewModel.changeContent(binding.edit.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.loadPosts()
            findNavController().navigateUp()
        }


        return binding.root
    }


}