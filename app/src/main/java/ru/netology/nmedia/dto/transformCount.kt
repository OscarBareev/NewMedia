package ru.netology.nmedia.dto

fun transformCount(number: Int): String {

    return when (number) {
        in 0..999 -> number.toString()
        in 1000..999_999 -> thousands(number)
        in 1_000_000..999_999_999 -> millions(number)
        else -> number.toString()[0] + "B"

    }
}

fun thousands(number: Int): String {

    val nString = number.toString()
    return when (nString.length) {
        4 ->
            if (number < 1100) {
                nString[0] + "K"
            } else {
                nString[0] + "." + nString[1] + "K"
            }
        5 -> nString[0] + "" + nString[1] + "K"
        6 -> nString[0] + "" + nString[1] + "" + nString[2] + "K"
        else -> nString
    }
}


fun millions(number: Int): String {
    val nString = number.toString()
    return if (number < 1_100_000) {
        nString[0] + "M"
    } else {
        nString[0] + "" + nString[1] + "M"
    }
}