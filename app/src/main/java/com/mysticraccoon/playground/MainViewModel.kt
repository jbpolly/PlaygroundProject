package com.mysticraccoon.playground

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {


    val currentTime = MutableLiveData<Int>()
    val countDownFlow = flow<Int> {
        val startingValue = 10
        var currentValue = startingValue
        emit(startingValue)
        while (currentValue > 0){
            delay(1000L)
            currentValue--
            emit(currentValue)
        }
    }

    init {
        collectFlow()
    }

    private fun collectFlow(){
        //when collect it execute for every emission of this flow
        viewModelScope.launch {
            countDownFlow.collect{ time ->
                currentTime.value = time
            }
        }

        //if the code block takes some time and is not finished yet, if we get another emission the previous computation will be cancelled
//        viewModelScope.launch {
//            countDownFlow.collectLatest{ time ->
//                delay(1500L)
//                println("The current time is $time")
//                //currentTime.value = time
//            }
//        }
        //We can use this if we have a flow that reflects the state of the UI

    }

}