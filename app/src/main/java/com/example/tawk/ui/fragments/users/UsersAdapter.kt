package com.example.tawk.ui.fragments.users

import android.content.Context
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.tawk.R
import com.example.tawk.data.GithubUser
import com.example.tawk.databinding.LayoutItemUserBinding
import com.seek.task.ui.Constants.GAME_VIEW_TYPE
import com.seek.task.ui.Constants.NETWORK_VIEW_TYPE
import kotlinx.coroutines.flow.MutableStateFlow

class UsersAdapter(private val context: Context) :
    PagingDataAdapter<GithubUser, RecyclerView.ViewHolder>(UserDiffCallback()) {

    var lastPosition = -1
    var listOfGithubUserNote = MutableStateFlow<List<GithubUser>>(emptyList())


    class UserDiffCallback : DiffUtil.ItemCallback<GithubUser>() {
        override fun areItemsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        setAnimation(holder, position)
        var isVisible = false


        (holder as UserViewHolder).binding.apply {
            user = item
            //Check if the saved user has note or not
            for (i in listOfGithubUserNote.value.indices) {
                if (item!!.login == listOfGithubUserNote.value[i].login && !listOfGithubUserNote.value[i].note.equals(
                        ""
                    )
                ) {
                    isVisible = true
                }
            }
            if (isVisible) {
                savedNoteIcon.visibility = View.VISIBLE
            } else {
                savedNoteIcon.visibility = View.GONE
            }

            Glide.with(context).load(item!!.avatar_url)
                .transition(
                    DrawableTransitionOptions.withCrossFade()
                ).into(userImage)
         /*   if (position % 4 == 0) {
                userImage.colorFilter = ColorMatrixColorFilter(NEGATIVE)
            }*/

            githubUserName.text = item.login
            githubUserURL.text = item.url
            githubUserDescription.text = item.following_url
            card.setOnClickListener {
                item.let {
                    onItemClick?.invoke(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            LayoutItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            NETWORK_VIEW_TYPE
        } else {
            GAME_VIEW_TYPE
        }
    }

    var onItemClick: ((GithubUser: GithubUser) -> Unit)? = null

    private fun setAnimation(holder: RecyclerView.ViewHolder, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.item_anim)
            holder.itemView.startAnimation(animation)
            lastPosition = position;
        }
    }

    private val NEGATIVE = floatArrayOf(
        -1.0f,
        0f,
        0f,
        0f,
        255f,
        0f,
        -1.0f,
        0f,
        0f,
        255f,
        0f,
        0f,
        -1.0f,
        0f,
        255f,
        0f,
        0f,
        0f,
        1.0f,
        0f
    )

    class UserViewHolder(val binding: LayoutItemUserBinding) : RecyclerView.ViewHolder(binding.root)
}