package com.example.tawk.local

import androidx.test.runner.AndroidJUnit4
import com.example.tawk.data.GithubUser
import com.example.tawk.AppTestModule
import com.example.tawk.network.service.UsersServices
import com.example.tawk.persistance.CachePreferences
import com.example.tawk.repos.UsersRepository
import com.example.tawk.room.UsersDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@HiltAndroidTest
@UninstallModules(AppTestModule::class)
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class)
class GithubDaoTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    val appliedUserDao = mockk<UsersDao>()
    val usersDao = mockk<UsersDao>()
    val services = mockk<UsersServices>()
    val cachePreferences = mockk<CachePreferences>()


    lateinit var instance: UsersRepository

    @Before
    fun init() {
        hiltAndroidRule.inject()
        instance = UsersRepository(services,cachePreferences,usersDao)
    }

    @Test
    fun testGetAllNotedUsers() = runBlockingTest {
        // Given
        val expectedUsers = listOf(
            GithubUser("user 1", 1),
            GithubUser("user 2", 2),
            GithubUser("user 3", 3)
        )
        // val githubNotedUserDao = mockk<GithubNotedUserDao>()
        coEvery { appliedUserDao.getAllUsers() } returns flowOf(expectedUsers)

        // When
        val users = appliedUserDao.getAllUsers()
        val actualUsers = users.toList().flatten()

        // Then
        assertEquals(expectedUsers, actualUsers)
    }

    @Test
    fun testInsertNotedUser() = runBlockingTest {
        // Given
        val githubUser = GithubUser("user 1", 1)

        // Mock the DAO's insertNotedUser function
        coEvery { appliedUserDao.insertUser(githubUser) } returns Unit

        // When
        instance.insertGithubUser(githubUser)

        // Then
        coVerify { appliedUserDao.insertUser(githubUser) }
    }

    @Test
    fun getAllCachedUsersTest() = runBlockingTest {
        val expectedUsers = listOf(
            GithubUser("user 1", 1),
            GithubUser("user 2", 2),
            GithubUser("user 3", 3)
        )
        coEvery { usersDao.getAllUsers() } returns flowOf(expectedUsers)

        // When
        val users = usersDao.getAllUsers()
        val actualUsers = users.toList().flatten()

        // Then
        assertEquals(expectedUsers, actualUsers)
    }

    @Test
    fun insertUserGithubUserDataTest() = runBlockingTest {
        // Given
        var githubUser = GithubUser()
        githubUser.login = "User 1"
        githubUser.id = 1

        coEvery { usersDao.insertUser(githubUser) } returns Unit
        instance.insertGithubUser(githubUser)
        coVerify { usersDao.insertUser(githubUser) }
    }
}