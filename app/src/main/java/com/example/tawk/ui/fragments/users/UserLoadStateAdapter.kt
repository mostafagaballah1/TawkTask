package com.example.tawk.ui.fragments.users

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tawk.databinding.LayoutItemLoadingBinding


class UserLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<UserLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding =
            LayoutItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return LoadStateViewHolder(binding, retry)
    }

    inner class LoadStateViewHolder(binding: LayoutItemLoadingBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private val progressBar: ProgressBar = binding.progressBar
        private val errorMsg: TextView = binding.errorMsg
        private val retry: Button = binding.retryButton.also {
            it.setOnClickListener {
                retry()
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                errorMsg.text = loadState.error.localizedMessage
            }

            progressBar.isVisible = loadState is LoadState.Loading
            retry.isVisible = loadState is LoadState.Error
            errorMsg.isVisible = loadState is LoadState.Error
        }
    }


}