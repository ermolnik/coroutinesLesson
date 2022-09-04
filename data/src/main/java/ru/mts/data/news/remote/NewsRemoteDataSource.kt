package ru.mts.data.news.remote

import kotlinx.coroutines.delay
import ru.mts.data.main.NetworkClient
import ru.mts.data.utils.runOperationCatching
import ru.mts.data.utils.Result

class NewsRemoteDataSource {
    suspend fun getNews(): Result<NewsDto.Response, Throwable> {
        return runOperationCatching {
            delay(3000L)
            NetworkClient.create().getSampleData(NewsDto.Request(1))
        }
    }
}