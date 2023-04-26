package com.example.tawk.ui.main

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.tawk.utils.NetworkConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 *
 * MainActivityViewModel manages loading state
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val networkConnectionUseCase: NetworkConnectionUseCase,) : ViewModel() {
    private val _loadingEvent = MutableStateFlow(false)
    val loadingEvent: StateFlow<Boolean> get() = _loadingEvent

    val networkStateLiveData: MutableLiveData<NetworkConnectionUseCase.NetworkStates> =
        MutableLiveData<NetworkConnectionUseCase.NetworkStates>().apply {
            value = NetworkConnectionUseCase.NetworkStates.NoNetwork
        }

    fun isLoadingVisible(isVisible: Boolean) {
        _loadingEvent.value = isVisible
    }

    /**
     * Network Connection States
     */
    fun updateNetworkState(state: NetworkConnectionUseCase.NetworkStates) {
        networkStateLiveData.value = state
    }

    fun registerNetworkConnectionCallback(context: Context) {
        networkConnectionUseCase.onStart(context)
    }

    fun unregisterNetworkConnectionCallback(context: Context) {
        networkConnectionUseCase.onDestroy(context)
    }

    fun observeNetworkConnection (
        lifecycleOwner: LifecycleOwner,
        observer: Observer<NetworkConnectionUseCase.NetworkStates>
    ) {
        networkConnectionUseCase.observe(
            lifecycleOwner,
            observer
        )
    }

    fun observeNetworkState(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<NetworkConnectionUseCase.NetworkStates>
    ) {
        networkStateLiveData.observe(
            lifecycleOwner,
            observer
        )
    }
}