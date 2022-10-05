package com.mysticraccoon.playground

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {


    val currentTime = MutableLiveData<Int>()
    val countDownFlow = flow<Int> {
        val startingValue = 5
        var currentValue = startingValue
        emit(startingValue)
        while (currentValue > 0){
            delay(1000L)
            currentValue--
            emit(currentValue)
        }
    }

    init {
        //collectFlow()
       // collectFlow2()
        //collectFlow3()
       // collectFlow4()
        bufferOperator()
    }

    private fun collectFlow(){

        //Same as launch and onEach before
//        countDownFlow.onEach {
//            println(it)
//        }.launchIn(viewModelScope)

        //when collect it execute for every emission of this flow
        viewModelScope.launch {
            countDownFlow.filter { time ->
                //we only want even values
                time%2 == 0
            }.map { time ->
                time*time
            }.onEach { time ->
                println(time)
            }.collect{ time ->
               // currentTime.value = time
                println("The current time is $time")
            }
        }
    }

    //terminal operators -> terminate the flow -> take the whole result of a flow and then do something with it
    private fun collectFlow2(){
        //when collect it execute for every emission of this flow
        viewModelScope.launch {
           val count =  countDownFlow.filter { time ->
                //we only want even values
                time%2 == 0
            }.map { time ->
                time*time
            }.onEach { time ->
                println(time)
            }.count{ time ->
                // count the values that match a specific condition
                time%2 == 0
            }
            println("Count is: $count")
        }
    }

    //other terminal operator REDUCE
    private fun collectFlow3(){
        //when collect it execute for every emission of this flow
        viewModelScope.launch {
            val reduceResult =  countDownFlow.reduce { accumulator, value ->
                //reduce executed for every emission
                accumulator + value
            }
            println("Reduce result is: $reduceResult")
        }
    }

    private fun collectFlow4(){
        //when collect it execute for every emission of this flow
        viewModelScope.launch {
            //initiate the accumulator with the fold initial value
            val reduceResult =  countDownFlow.fold(100) { accumulator, value ->
                //reduce executed for every emission
                accumulator + value
            }
            println("Reduce result is: $reduceResult")
        }
    }


    //[[1,2], [1,2,3]]
    //if we flatten this list
    //[1,2,1,2,3]
    private fun flatteningOperator(){
        //we don't flatten list, we flatten flows
        val flow1 = flow{
            emit(1)
            delay(500L)
            emit(2)
        }

        viewModelScope.launch {
            flow1.flatMapConcat {  value ->
                //this function needs to return a flow
                flow {
                    emit(value+1)
                    delay(500L)
                    emit(value+2)
                }
            }.collect{ value->
                println("The valus is $value")

            }
        }
    }

//    private fun flatteningOperator2(){
//        //we don't flatten list, we flatten flows
//        val flow1 = (1..5).asFlow()
//
//        viewModelScope.launch {
//            flow1.flatMapConcat {  value ->
//                //this function needs to return a flow
//                getRecipeById(id)
//            }.collect{ value->
//                println("The valus is $value")
//
//            }
//        }
//    }

    private fun bufferOperator(){
        val flow = flow{
            delay(250L)
            emit("Appetizer")
            delay(1000L)
            emit("Main dish")
            delay(100L)
            emit("Dessert")
        }


        //if the collect block finishes then we are ready to emit the next value
        //buffer makes collector runs in a different thread then the producer
        //this means that the flow can go on while the collector did not finish yet
        viewModelScope.launch {
            flow.onEach {
                println("FLOW: $it is delivered")
            }.buffer().collect{
                println("Now eating $it")
                delay(1500L)
                println("Finished eating $it")
            }
        }
    }

}