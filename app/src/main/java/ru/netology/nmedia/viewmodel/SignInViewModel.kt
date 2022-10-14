package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.model.AuthModelState
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao())

    private val _authModelState = MutableLiveData<AuthModelState>()
    val authModelState: LiveData<AuthModelState>
        get() = _authModelState


    fun updateUser(login: String, pass: String) = viewModelScope.launch {
        try {
            repository.updateUser(login, pass)
            _authModelState.value = AuthModelState(isSignIn = true)
        } catch (e: Exception) {
            _authModelState.value = AuthModelState(isSignIn = false, error = true)
        }
    }
}