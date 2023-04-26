package com.example.tawk.ui.fragments.users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.tawk.data.GithubUser
import com.example.tawk.repos.UsersRepository
import com.example.tawk.room.UsersDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MainActivityViewModel manages loading state
 */
@HiltViewModel
class UsersViewModel @Inject constructor(repo: UsersRepository, private val userDao: UsersDao) :
    ViewModel() {

    private val _cachedUsersList = MutableStateFlow<List<GithubUser>>(emptyList())
    val cachedUsersList = _cachedUsersList.asStateFlow()

    val explore = repo.getAllUsersFromRemote(20).cachedIn(viewModelScope)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllUsers().distinctUntilChanged().collect { listOfUsers ->
                if (listOfUsers.isNullOrEmpty()) {
                    Log.d("listOfUsers", ": Empty listOfUsers ")
                } else {
                    _cachedUsersList.value = listOfUsers
                    Log.d("listOfUsers", ":${cachedUsersList.value.size} ")
                }
            }
        }
    }

    suspend fun insertUser(githubUser: GithubUser) = userDao.insertUser(githubUser)

    fun filterDataBySearch(searchValue: String): List<GithubUser> {
        val searchedGithubUserList = mutableListOf<GithubUser>()
        for (i in _cachedUsersList.value.indices){
            if (_cachedUsersList.value[i].login!!.contains(searchValue) || _cachedUsersList.value[i].note!!.contains(searchValue) ){
                searchedGithubUserList.add(toGithubUser(_cachedUsersList.value[i]))
            }
        }
        return searchedGithubUserList
    }

    fun toGithubUser(githubUserNote: GithubUser): GithubUser {
        return GithubUser(
            githubUserNote.login,
            githubUserNote.id,
            githubUserNote.node_id,
            githubUserNote.avatar_url,
            githubUserNote.gravatarId,
            githubUserNote.url,
            githubUserNote.html_url,
            githubUserNote.followers_url,
            githubUserNote.following_url,
            githubUserNote.gists_url,
            githubUserNote.starred_url,
            githubUserNote.subscriptions_url,
            githubUserNote.organizations_url,
            githubUserNote.repos_url,
            githubUserNote.events_url,
            githubUserNote.received_events_url,
            githubUserNote.type,
            githubUserNote.site_admin,
            githubUserNote.name,
            githubUserNote.company,
            githubUserNote.blog,
            githubUserNote.location,
            githubUserNote.email,
            githubUserNote.hireable,
            githubUserNote.bio,
            githubUserNote.twitter_username,
            githubUserNote.public_repos,
            githubUserNote.public_gists,
            githubUserNote.followers,
            githubUserNote.following,
            githubUserNote.created_at,
            githubUserNote.updated_at,
            githubUserNote.note
        )
    }
}