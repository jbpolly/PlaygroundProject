package com.mysticraccoon.playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mysticraccoon.playground.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Type of objects we emit in that flow
        val flow = flow<String> {
            //everything inside the flow block happens in a coroutine
            for(i in 1..10){
                emit("Hello world")
                delay(1000L)
            }
        }
        //this is our producer

        //the consumer must be launched on a coroutine
//        GlobalScope.launch {
//        //this collect function will give us the values emitted
//            flow.collect{
//                println(it)
//                //if we delay the consume of the values
//                //the producer will wait and buffer that data to lead to the consumer when the data is ready
//                delay(2000L)
//            }
//        }
        //This takes 3 seconds to display instead of only 2. Why?
        //Because by default the producer and consumer runs in the same coroutine
        //So if the producer suspends then the consumer will also delays
        //To avoid this we can use buffer

        //this buffer makes the producer and consumer runs in different coroutines
        GlobalScope.launch {
            //this collect function will give us the values emitted
            flow.buffer().collect{
                println(it)
                //if we delay the consume of the values
                //the producer will wait and buffer that data to lead to the consumer when the data is ready
                delay(2000L)
            }
        }
        //if we consistently gets more data from server then you can consume you can use this buffer operator with caution as to not overflow the buffer size


        val flow2 = flow {
            for(i in 1..10){
                emit(i)
                delay(1000L)
            }
        }

        GlobalScope.launch {
            flow2.buffer().filter {
                it % 2 == 0
            }.map{
                it * it
            }.collect{
                println(it)
                delay(2000L)
            }
        }

    }

    //with plain coroutines, when we have suspend functions it returns something and then that function is over
    suspend fun helloWorld(): String{
        return "Hello World"
    }

    //Flows actually take that concept to the next level because we can return several values at different times
    //We EMIT values
    //we have a producer and a consumer
    //a producer emit new values and the consumer consume them and do something with these values
    //they fully support back pressure: mechanism where the server sends more data at a given time than the client can process
    //it will make sure that the producer only gives data to the consumer when the consumer can process that



}