package ru.mts.data.news.db

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.mts.data.main.AppDatabase
import ru.mts.data.utils.Result
import ru.mts.data.utils.runOperationCatching

class NewsLocalDataSource(private val context: Context) {
    suspend fun getNews(): Result<NewsEntity, Throwable> {
        return runOperationCatching {
            delay(1000L)
            withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(context).newsDao().getById(1) ?: NewsEntity(2)
            }
        }
    }
}
