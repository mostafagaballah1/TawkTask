package com.example.tawk.data

import androidx.room.Entity

@Entity(tableName = "users_table", primaryKeys = ["id"])
data class GithubUser(
    var login: String = "",
    var id: Int? = 0,
    var node_id: String? = "",
    var avatar_url: String? = "",
    var gravatarId: String? = "",
    var url: String? = "",
    var html_url: String? = "",
    var followers_url: String? = "",
    var following_url: String? = "",
    var gists_url: String? = "",
    var starred_url: String? = "",
    var subscriptions_url: String? = "",
    var organizations_url: String? = "",
    var repos_url: String? = "",
    var events_url: String? = "",
    var received_events_url: String? = "",
    var type: String? = "",
    var site_admin: Boolean? = null,
    var name: String? = null,
    var company: String? = null,
    var blog: String? = null,
    var location: String? = null,
    var email: String? = null,
    var hireable: Boolean? = null,
    var bio: String? = null,
    var twitter_username: String? = null,
    var public_repos: Int? = null,
    var public_gists: Int? = null,
    var followers: Int? = null,
    var following: Int? = null,
    var created_at: String? = null,
    var updated_at: String? = null,
    var note: String? = ""
) : java.io.Serializable{

}


