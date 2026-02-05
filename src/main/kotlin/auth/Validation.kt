package com.zhdanon.auth

private val emailRegex =
    Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

fun validateEmail(email: String): String? =
    when {
        email.isBlank() -> "Email не может быть пустым"
        !emailRegex.matches(email) -> "Некорректный формат email"
        else -> null
    }

fun validatePassword(password: String): String? =
    when {
        password.length < 6 -> "Пароль должен быть не короче 6 символов"
        else -> null
    }