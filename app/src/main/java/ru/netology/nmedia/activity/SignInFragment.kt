package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentSignInBinding
import ru.netology.nmedia.viewmodel.SignInViewModel


class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding: FragmentSignInBinding =
            FragmentSignInBinding.inflate(inflater, container, false)

        val viewModel: SignInViewModel by viewModels(ownerProducer = ::requireParentFragment)


        binding.signInBtn.setOnClickListener {
            val logging: String = binding.edTxtLogin.text.toString()
            val pass: String = binding.edTxtPassword.text.toString()

            viewModel.updateUser(logging, pass)
        }

        viewModel.authModelState.observe(viewLifecycleOwner) { state ->
            if (state.isSignIn) findNavController().navigateUp()
            if (state.error) {
                Toast.makeText(activity, "Wrong login or password", Toast.LENGTH_LONG).show()
            }

        }





        return binding.root
    }


}