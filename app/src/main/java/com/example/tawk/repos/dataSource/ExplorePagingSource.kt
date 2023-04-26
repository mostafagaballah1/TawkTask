package com.example.tawk.repos.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tawk.data.GithubUser
import com.example.tawk.network.service.UsersServices
import com.example.tawk.persistance.CachePreferences
import com.example.tawk.utils.GITHUB_STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

class ExplorePagingSource(
    private val services: UsersServices,
    private val cachePreferences: CachePreferences
) : PagingSource<Int, GithubUser>() {

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUser> {
            val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
            return try {
                val response = services.getUsersByPage(position).map { it }
                val nextKey = position + 30
                LoadResult.Page(
                    data = response,
                    prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = nextKey
                )
            } catch (exception: IOException) {
                return LoadResult.Error(exception)
            } catch (exception: HttpException) {
                return LoadResult.Error(exception)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, GithubUser>): Int? {
            // We need to get the previous key (or next key if previous is null) of the page
            // that was closest to the most recently accessed index.
            // Anchor position is the most recently accessed index
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }
        }
}