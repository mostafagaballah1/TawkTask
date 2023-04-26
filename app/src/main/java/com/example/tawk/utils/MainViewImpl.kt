package com.example.tawk.utils

import android.util.Log
import javax.inject.Inject


class MainViewImpl @Inject constructor(): MainView {
    override fun changeNetworkState(state: MainView.State) {
        when (state) {
            is MainView.State.Connected -> {
                Log.d("NetWorkStateIs","Connected")
            }
            is MainView.State.NoNetwork -> {
                Log.d("NetWorkStateIs","NoNetwork")
            }
        }
    }
}