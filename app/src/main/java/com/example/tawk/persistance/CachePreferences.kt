package com.example.tawk.persistance

import com.example.tawk.data.GithubUser
import kotlinx.coroutines.flow.Flow


interface CachePreferences {

    fun isAlreadyCachedFirstPage(): Flow<Boolean>
    fun isAlreadyCachedSecondPage(): Flow<Boolean>

    fun isReadFromCacheForTheFirstTime(): Boolean

    fun getCachedFirstPage(): Flow<String>
    suspend fun saveToCacheFirstPage(githubUsers: List<GithubUser>)

    fun getCachedSecondPage(): Flow<String>
    suspend fun saveToCacheSecondPage(githubUsers: List<GithubUser>)


}