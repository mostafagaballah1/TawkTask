package com.example.tawk.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber
import javax.inject.Inject


class NetworkConnectionUseCaseImpl @Inject constructor() : NetworkConnectionUseCase {

    private val networkBroadcastReceiver: BroadcastReceiver
    private val networkCallback: ConnectivityManager.NetworkCallback
    private var state: MutableLiveData<NetworkConnectionUseCase.NetworkStates> =
        MutableLiveData<NetworkConnectionUseCase.NetworkStates>().apply {
            value = NetworkConnectionUseCase.NetworkStates.NoNetwork
        }

    init {
        networkBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent?) {
                val connectivityManager =
                    ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                val netInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                netInfo?.let {
                    state.postValue(
                        when {
                            it.isConnected ||
                                    it.isConnectedOrConnecting -> NetworkConnectionUseCase.NetworkStates.Connected
                            else -> NetworkConnectionUseCase.NetworkStates.NoNetwork
                        }
                    )
                } ?: kotlin.run {
                    state.postValue(NetworkConnectionUseCase.NetworkStates.NoNetwork)
                }
            }
        }

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            private val set: MutableSet<Network> = mutableSetOf()

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                set.add(network)
                state.postValue(NetworkConnectionUseCase.NetworkStates.Connected)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                state.postValue(NetworkConnectionUseCase.NetworkStates.NoNetwork)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                set.remove(network)
                if (set.isEmpty()) {
                    state.postValue(NetworkConnectionUseCase.NetworkStates.NoNetwork)
                }
            }
        }
    }


    override fun onStart(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        netInfo?.let {
            state.value = (when {
                it.isConnected || it.isConnectedOrConnecting -> NetworkConnectionUseCase.NetworkStates.Connected
                else -> NetworkConnectionUseCase.NetworkStates.NoNetwork
            })
        } ?: kotlin.run {
            state.value = NetworkConnectionUseCase.NetworkStates.NoNetwork
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            context.registerReceiver(
                networkBroadcastReceiver,
                filter
            )
        } else {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerNetworkCallback(
                NetworkRequest.Builder().build(),
                networkCallback
            )
        }
    }

    override fun onDestroy(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                context.unregisterReceiver(networkBroadcastReceiver)
            } catch (exception: Exception) {
                Timber.d(NetworkConnectionUseCaseImpl::class.simpleName, exception.message)
            }
        } else {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            try {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            } catch (exception: IllegalArgumentException) {
                Timber.d(NetworkConnectionUseCaseImpl::class.simpleName, exception.message)
            }
        }
    }

    override fun observe(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<NetworkConnectionUseCase.NetworkStates>
    ) {
        state.observe(lifecycleOwner, observer)
    }
}