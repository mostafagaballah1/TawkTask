package com.example.tawk.ui.fragments.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tawk.data.GithubUser
import com.example.tawk.repos.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repo: UsersRepository) : ViewModel() {


    var githubUserData = MutableLiveData<GithubUser>()
    lateinit var githubSavedUserData : GithubUser

    fun saveGithubUser(githubUser: GithubUser) {
        viewModelScope.launch {
            repo.updateGithubUser(githubUser)
        }
    }

    fun getUserDetails(username: String) {
        viewModelScope.launch {
            githubUserData.value = repo.getGithubUserDetails(username)
        }
    }

    fun isExist():Boolean {
        var result = false
        viewModelScope.launch {
            result = repo.isExists()
        }
        return result
    }

    fun getSavedUserDetails(username: String): GithubUser {
        viewModelScope.launch {
            githubSavedUserData = repo.getSavedUser(username)
            Log.d("Get saved user ", githubSavedUserData.login)
        }
        return githubSavedUserData
    }
}