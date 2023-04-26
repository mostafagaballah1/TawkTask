package com.example.tawk.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tawk.data.GithubUser


@Database(
    entities = [GithubUser::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
}