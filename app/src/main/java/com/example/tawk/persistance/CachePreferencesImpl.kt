package com.example.tawk.persistance

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.tawk.data.GithubUser
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CachePreferencesImpl(private val dataStore: DataStore<Preferences>) : CachePreferences {

    var isReadFromCache = false
    override fun isReadFromCacheForTheFirstTime(): Boolean = isReadFromCache

    override fun isAlreadyCachedFirstPage(): Flow<Boolean> =
            dataStore.data.map {
                it[IS_ALREADY_FIRST_CACHED] ?: false
            }

    override fun isAlreadyCachedSecondPage(): Flow<Boolean> =
            dataStore.data.map {
                it[IS_ALREADY_SECOND_CACHED] ?: false
            }

    override fun getCachedFirstPage(): Flow<String> {
        return dataStore.data.map {
            it[FIRST_PAGE_CACHED] ?: ""
        }
    }

    override suspend fun saveToCacheFirstPage(githubUsers: List<GithubUser>) {
        val json = Gson().toJson(githubUsers)
        dataStore.edit {
            it[FIRST_PAGE_CACHED] = json
            it[IS_ALREADY_FIRST_CACHED] = true
        }

    }

    override fun getCachedSecondPage(): Flow<String> {
        return dataStore.data.map {
            isReadFromCache = true
            it[SECOND_PAGE_CACHED] ?: ""
        }
    }

    override suspend fun saveToCacheSecondPage(githubUsers: List<GithubUser>) {
        val json = Gson().toJson(githubUsers)
        dataStore.edit {
            it[SECOND_PAGE_CACHED] = json
            it[IS_ALREADY_SECOND_CACHED] = true
            isReadFromCache = true
        }
    }

    companion object {

        val IS_ALREADY_FIRST_CACHED = booleanPreferencesKey("is_first_page_already_cached")
        val FIRST_PAGE_CACHED = stringPreferencesKey("first_page_cached")

        val IS_ALREADY_SECOND_CACHED = booleanPreferencesKey("is_second_page_already_cached")
        val SECOND_PAGE_CACHED = stringPreferencesKey("second_page_cached")


    }


}