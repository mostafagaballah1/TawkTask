package com.example.tawk.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tawk.data.GithubUser
import kotlinx.coroutines.flow.Flow


@Dao
abstract class UsersDao : BaseDao<GithubUser> {

    //Get all Cached users
    @Query("SELECT * FROM users_table")
    abstract fun getAllUsers(): Flow<List<GithubUser>>

    @Query("SELECT EXISTS(SELECT * FROM users_table)")
    abstract fun isExists(): Boolean

    @Query("SELECT * FROM users_table WHERE login = :username")
    abstract fun getUser(username: String): GithubUser

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertUser(githubUser: GithubUser)

    @Query("UPDATE users_table SET note = :note WHERE login = :userId")
    abstract suspend fun updateUser( note: String, userId: String)
}