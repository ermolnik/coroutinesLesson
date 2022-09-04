package ru.ermolnik.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.mts.data.news.repository.NewsRepository
import ru.mts.data.utils.doOnError
import ru.mts.data.utils.doOnSuccess

class NewsViewModel(repository: NewsRepository) : ViewModel() {
    private val _state: MutableStateFlow<NewsState> = MutableStateFlow(NewsState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getNews().collect {
                it.doOnError { error ->
                    _state.emit(NewsState.Error(error))
                }.doOnSuccess { news ->
                    _state.emit(NewsState.Content(news.id))
                }
            }
        }
    }
}
