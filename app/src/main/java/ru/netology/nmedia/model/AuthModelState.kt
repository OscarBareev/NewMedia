package ru.netology.nmedia.model

data class AuthModelState(
    val isSignIn: Boolean = false,
    val error: Boolean = false,
)