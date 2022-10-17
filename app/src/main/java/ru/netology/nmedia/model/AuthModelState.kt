package ru.netology.nmedia.model

data class AuthModelState(
    val isAccessible: Boolean = false,
    val error: Boolean = false,
)