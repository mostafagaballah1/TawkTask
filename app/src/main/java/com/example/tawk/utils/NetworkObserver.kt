package com.example.tawk.utils

import androidx.lifecycle.Observer

class NetworkObserver(
    private val rootView: MainView
) : Observer<NetworkConnectionUseCase.NetworkStates> {


    override fun onChanged(action : NetworkConnectionUseCase.NetworkStates?) {
        when (action) {
            is NetworkConnectionUseCase.NetworkStates.Connected -> {
                val state = MainView.State.Connected
                rootView.changeNetworkState(state)
            }
            is NetworkConnectionUseCase.NetworkStates.NoNetwork -> {
                val state = MainView.State.NoNetwork
                rootView.changeNetworkState(state)
            }
        }
    }
}