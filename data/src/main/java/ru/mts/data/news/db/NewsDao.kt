package ru.mts.data.news.db

import androidx.room.*


@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getAll(): List<NewsEntity?>?

    @Query("SELECT * FROM news WHERE id = :id")
    fun getById(id: Long): NewsEntity?

    @Insert
    fun insert(news: NewsEntity?)

    @Update
    fun update(news: NewsEntity?)

    @Delete
    fun delete(news: NewsEntity?)
}