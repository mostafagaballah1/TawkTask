package com.example.tawk.ui.fragments.details

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.tawk.R
import com.example.tawk.data.GithubUser
import com.example.tawk.databinding.FragmentDetailsBinding
import com.example.tawk.ui.BaseFragment
import com.example.tawk.ui.main.MainViewModel
import com.example.tawk.utils.NetworkConnectionUseCase
import com.example.tawk.utils.toast
import dagger.hilt.android.AndroidEntryPoint

/**
 * Details fragment that contains user information
 */

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding>(R.layout.fragment_details) {

    private val viewModel: DetailsViewModel by viewModels()
    private val activityViewModel: MainViewModel by viewModels()
    private var isOnlineStatePreviewAvailable = false
    var cachedGithubUser: GithubUser? = null

    override fun onViewCreationCompleted() {

        activityViewModel.observeNetworkConnection(
            viewLifecycleOwner,
            Observer { it ->
                activityViewModel.updateNetworkState(it)
                when (it) {
                    NetworkConnectionUseCase.NetworkStates.Connected -> {
                        binding.noInternetMessage.visibility = View.GONE
                        isOnlineStatePreviewAvailable = true

                        val bundle = arguments
                        if (bundle != null) {
                            // Retrieve data here
                            val githubUserName = bundle.getString("githubUserName")
                            binding.userScreenName.text = githubUserName
                            viewModel.getUserDetails(githubUserName!!)
                            viewModel.githubUserData.observe(
                                viewLifecycleOwner,
                                Observer<GithubUser> { remoteGithubUser ->
                                    if (viewModel.isExist())
                                        cachedGithubUser =
                                            viewModel.getSavedUserDetails(githubUserName)
                                    updateUI(remoteGithubUser)
                                }
                            )
                        }
                    }
                    NetworkConnectionUseCase.NetworkStates.NoNetwork -> {
                        binding.noInternetMessage.visibility = View.VISIBLE
                    }
                }
            }
        )
    }

    private fun updateUI(githubUser: GithubUser) {
        githubUser.let {
            Glide.with(requireContext()).load(githubUser.avatar_url)
                .transition(DrawableTransitionOptions.withCrossFade()).into(binding.userImage)
            binding.followersNo.text = githubUser.followers.toString()
            binding.followeingNo.text = githubUser.following.toString()
            binding.githubUserName.text = githubUser.login
            binding.companyNameDesc.text = githubUser.company

            //handle user note
            if (cachedGithubUser != null) {
                binding.noteText.setText(cachedGithubUser!!.note)
            }

            binding.blogDesc.text = githubUser.blog
            binding.imageBackIcon.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            binding.saveNote.setOnClickListener {
                if (binding.noteText.text.toString().isNotEmpty()) {
                    githubUser.note = binding.noteText.text.toString()
                    viewModel.saveGithubUser(githubUser)
                    toast(getString(R.string.saved_success))
                } else {
                    toast(getString(R.string.enter_note_msg))
                }
            }
        }
    }
}