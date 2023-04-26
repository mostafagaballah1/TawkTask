package com.example.tawk.viewmodels

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.runner.AndroidJUnit4
import com.example.tawk.AppTestModule
import com.example.tawk.testUtils.TestUtils
import com.example.tawk.ui.main.MainViewModel
import com.example.tawk.utils.NetworkConnectionUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@HiltAndroidTest
@UninstallModules(AppTestModule::class)
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class)
class MainVMTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    lateinit var instance: MainViewModel
    val networkConnectionUseCase: NetworkConnectionUseCase = mockk(relaxed = true)
    val mockLifecycleOwner: LifecycleOwner = mockk(relaxed = true)
    val mockContext: Context = mockk(relaxed = true)

    @Before
    fun init() {
        hiltAndroidRule.inject()
        instance = MainViewModel(networkConnectionUseCase)
    }

    @After
    fun unRegister() {
        unmockkAll()
    }

    @Test
    fun UpdateNetworkStateWithParameter() {
        val mockState = mockk<NetworkConnectionUseCase.NetworkStates>(relaxed = true)
        val mockLiveData =
            mockk<MutableLiveData<NetworkConnectionUseCase.NetworkStates>>(relaxed = true)
        val mockObserver =
            mockk<Observer<NetworkConnectionUseCase.NetworkStates>>(relaxed = true)

        TestUtils.setProperty(instance, "networkStateLiveData", mockLiveData)

        mockLiveData.observeForever(mockObserver)
        instance.updateNetworkState(mockState)
        verify { mockLiveData.value = mockState }

        mockLiveData.removeObserver(mockObserver)
    }

    @Test
    fun ObserveNetworkConnectionWithParameter() {
        val mockObserver = mockk<Observer<NetworkConnectionUseCase.NetworkStates>>(relaxed = true)
        instance.observeNetworkConnection(mockLifecycleOwner, mockObserver)
        verify { networkConnectionUseCase.observe(mockLifecycleOwner, mockObserver) }
    }


    @Test
    fun UnRegisterNetworkConnectionCallbackWithParameter() {
        instance.unregisterNetworkConnectionCallback(mockContext)
        verify { networkConnectionUseCase.onDestroy(mockContext) }
    }
}