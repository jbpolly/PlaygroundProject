package com.mysticraccoon.playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.io.File

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            delay(1000L)
            //delay is a suspend function
            //They can only be executed inside another suspend function or inside a coroutine
        }
        //Throws error
        //delay(1000L)

        //Error
        //doNetworkCall()

        GlobalScope.launch {
            val networkCallAnswer = doNetworkCall()
            val networkCallAnswer2 = doNetworkCall2()
            Log.d(TAG, networkCallAnswer)
            Log.d(TAG, networkCallAnswer2)
            //Because these two delays were inside the same coroutine, they will influence each other
            //In the end, this code will take 6 seconds to print the string instead of only 3
        }
    }

    suspend fun doNetworkCall(): String{
        delay(3000L)
        return "This is the answer"
    }

    suspend fun doNetworkCall2(): String{
        delay(3000L)
        return "This is the answer 2"
    }


}