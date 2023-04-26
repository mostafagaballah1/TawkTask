package com.example.tawk.repos

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.tawk.data.GithubUser
import com.example.tawk.persistance.CachePreferences
import com.example.tawk.network.service.UsersServices
import com.example.tawk.repos.dataSource.ExplorePagingSource
import com.example.tawk.room.UsersDao
import kotlinx.coroutines.flow.Flow


class UsersRepository(
    private val services: UsersServices,
    private val cachePreferences: CachePreferences,
    private val usersDao: UsersDao
) {
    fun getAllUsersFromRemote(pageSize: Int): Flow<PagingData<GithubUser>> = Pager(
        PagingConfig(pageSize)
    ) {
        ExplorePagingSource(services, cachePreferences)
    }.flow

    fun getAllUsers(): Flow<List<GithubUser>> = usersDao.getAllUsers()

    fun isExists(): Boolean = usersDao.isExists()

    fun getSavedUser(username: String):GithubUser =
        usersDao.getUser(username)

    suspend fun updateGithubUser(githubUser: GithubUser) =
        usersDao.updateUser(githubUser!!.note!!, githubUser!!.login!!)

    suspend fun insertGithubUser(githubUser: GithubUser) = usersDao.insertUser(githubUser)

    suspend fun getGithubUserDetails(userName: String) = services.getGithubUserDetails(userName)

}