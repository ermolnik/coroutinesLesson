package ru.ermolnik.news

sealed class NewsState {
    object Loading: NewsState()
    data class Error(val throwable: Throwable): NewsState()
    data class Content(val id: Int): NewsState()
}