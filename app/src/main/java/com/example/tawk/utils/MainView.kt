package com.example.tawk.utils




interface MainView{

    fun changeNetworkState(state: State)
    sealed class State {
        object Connected : State()
        object NoNetwork : State()
    }

}