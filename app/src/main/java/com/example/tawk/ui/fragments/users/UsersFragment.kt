package com.example.tawk.ui.fragments.users

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.example.tawk.R
import com.example.tawk.databinding.FragmentUsersBinding
import com.example.tawk.ui.BaseFragment
import com.example.tawk.ui.main.MainViewModel
import com.example.tawk.utils.MainView
import com.example.tawk.utils.NetworkConnectionUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class UsersFragment : BaseFragment<FragmentUsersBinding>(R.layout.fragment_users) {

    private val viewModel by viewModels<UsersViewModel>()
    private val activityViewModel: MainViewModel by viewModels()
    private var isOnlineStatePreviewAvailable = false

    @Inject
    lateinit var usersAdapter: UsersAdapter

    @Inject
    lateinit var view: MainView
    var isSwipeRefreshing = false

    override fun onViewCreationCompleted() {

        initView()
        activityViewModel.observeNetworkConnection(
            viewLifecycleOwner,
            Observer {
                activityViewModel.updateNetworkState(it)
                when (it) {
                    NetworkConnectionUseCase.NetworkStates.Connected -> {
                        isOnlineStatePreviewAvailable = true
                        binding.noInternetMessage.visibility = View.GONE
                        binding.shimmerFrameLayout.visibility = View.GONE
                        binding.shimmerFrameLayout.stopShimmerAnimation()
                        lifecycleScope.launchWhenCreated {
                            viewModel.explore.collectLatest { listPagingData ->
                                usersAdapter.submitData(lifecycle, listPagingData)
                                val list = usersAdapter.snapshot().items
                                for (i in list.indices) {
                                    viewModel.insertUser(list[i])
                                }
                                binding.searchIcon.setOnClickListener {
                                    if (binding.textEditSearch.text.toString().isNotEmpty()) {
                                        usersAdapter.submitData(lifecycle, listPagingData)
                                        val filterList = viewModel.filterDataBySearch(
                                            binding.textEditSearch.text.toString()
                                        )
                                        val pagingListUsers = PagingData.from(filterList)
                                        usersAdapter.submitData(lifecycle, pagingListUsers)
                                    } else {
                                        usersAdapter.submitData(lifecycle, listPagingData)
                                    }
                                }
                            }
                        }
                    }
                    NetworkConnectionUseCase.NetworkStates.NoNetwork -> {
                        binding.shimmerFrameLayout.visibility = View.GONE
                        binding.noInternetMessage.visibility = View.VISIBLE
                        if (!isOnlineStatePreviewAvailable) {
                            lifecycleScope.launchWhenStarted {
                                viewModel.cachedUsersList.collectLatest { listPagingDataOffline ->
                                    //--- Handle Shimmer Effect or Load Cached Data If Exist
                                    if (listPagingDataOffline.isEmpty()){
                                        binding.shimmerFrameLayout.visibility = View.VISIBLE
                                        binding.shimmerFrameLayout.startShimmerAnimation()
                                    }else{
                                        binding.shimmerFrameLayout.visibility = View.GONE
                                        binding.shimmerFrameLayout.stopShimmerAnimation()
                                        val pagingListUsers = PagingData.from(listPagingDataOffline)
                                        usersAdapter.submitData(lifecycle, pagingListUsers)

                                        binding.searchIcon.setOnClickListener {
                                            if (binding.textEditSearch.text.toString().isNotEmpty()) {
                                                val filterList = viewModel.filterDataBySearch(
                                                    binding.textEditSearch.text.toString()
                                                )
                                                val pagingListUsers = PagingData.from(filterList)
                                                usersAdapter.submitData(lifecycle, pagingListUsers)
                                            } else {
                                                usersAdapter.submitData(lifecycle, pagingListUsers)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )

        lifecycleScope.launch {
            viewModel.cachedUsersList.collect {
                if (it.isNotEmpty()) {
                    usersAdapter.listOfGithubUserNote.value = viewModel.cachedUsersList.value
                    usersAdapter.notifyDataSetChanged()
                }
            }
        }

        binding.swipeRefresh.setOnRefreshListener {

        }

        lifecycleScope.launchWhenStarted {
            usersAdapter.loadStateFlow.collectLatest { loadStates ->

                val isError = loadStates.refresh is LoadState.Error

                if (isError) {
                    showTostify("No Internet !", "#d9534f", false)
                } else {
                    dismissTostify()
                }

                binding.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading

                if (!binding.swipeRefresh.isRefreshing && isSwipeRefreshing && !isError) {
                    showTostify("Refresh completed !", "#4BB543", true)
                    isSwipeRefreshing = false
                }
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            isSwipeRefreshing = true
            usersAdapter.refresh()
        }

        usersAdapter.onItemClick = { user ->
            var bundle = Bundle()
            bundle.putString("githubUserName", user.login)
            val navHostFragment =
                requireActivity().supportFragmentManager.findFragmentById(R.id.auth_nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.detailsFragment, bundle)
        }
    }

    private fun initView() {
        binding.list.apply {
            setHasFixedSize(true)
            adapter = usersAdapter.withLoadStateFooter(UserLoadStateAdapter(usersAdapter::retry))
        }
    }

    suspend fun showTostify(message: String, color: String, autoDismiss: Boolean) {
        binding.include.messageBg.setBackgroundColor(Color.parseColor(color))
        binding.include.messageTv.text = message
        binding.include.messageBg.animate().translationY(-80f).duration = 200L
        binding.include.messageTv.animate().translationY(-80f).duration = 200L
        delay(2000L)
        if (autoDismiss) {
            binding.include.messageBg.animate().translationY(80f).duration = 500L
            binding.include.messageTv.animate().translationY(80f).duration = 500L
        }

    }

    fun dismissTostify() {
        //Clear animation
        binding.include.messageBg.animate().translationY(0f)
        binding.include.messageTv.animate().translationY(0f)
    }
}