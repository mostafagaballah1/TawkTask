package com.example.tawk.ui.main

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.tawk.R
import com.example.tawk.databinding.ActivityMainBinding
import com.example.tawk.utils.MainView
import com.example.tawk.utils.NetworkObserver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * MainActivity container and start point for this app
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val activityViewModel: MainViewModel by viewModels()
    @Inject
    lateinit var view : MainView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = getResources().getColor(R.color.colorPrimary)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            window.addFlags(flags)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initializeView()
        activityViewModel.registerNetworkConnectionCallback(applicationContext)
        bindObservers()
    }

    private fun bindObservers() {
        activityViewModel.observeNetworkConnection(
            this@MainActivity,
            Observer {
                activityViewModel.updateNetworkState(it)
            }
        )
        activityViewModel.observeNetworkState(
            this@MainActivity,
            NetworkObserver(view)
        )
    }

    private fun initializeView() = with(binding) {
        lifecycleOwner = this@MainActivity
        viewModel = activityViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        activityViewModel.unregisterNetworkConnectionCallback(applicationContext)
    }
}