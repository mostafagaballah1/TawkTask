package com.example.tawk.utils

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer


interface NetworkConnectionUseCase {

    fun onStart(context: Context)

    fun onDestroy(context: Context)

    fun observe(lifecycleOwner: LifecycleOwner,
                observer: Observer<NetworkStates>)

    sealed class NetworkStates {
        object Connected: NetworkStates()
        object NoNetwork: NetworkStates()
    }

}