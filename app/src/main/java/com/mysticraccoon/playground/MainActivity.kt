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

        //our first coroutine

        //launch is a coroutine builder. It launches a new coroutine concurrently with the rest of the code, which continues to work independently.
        //the launch builder CANNOT be called outside a CoroutineScope


        //this coroutine will live as long as our application live
        //if the coroutine finishes it job before the application is killed then it is all good
        //but if the coroutine still have instructions to execute and the app is killed this will generate a problem
        GlobalScope.launch {
            //will be started on a separate thread
            Log.d(TAG, "Coroutine says 1 hello from thread ${Thread.currentThread().name}" )
        }

        Log.d(TAG, "Hello from thread ${Thread.currentThread().name}" )

        //================================================================

        //Delay is kind of a sleep function for coroutines
        GlobalScope.launch {
            //Delay works different from a thread sleep because it will only pause the current coroutine and not pause the whole thread
            delay(3000)
            Log.d(TAG, "Coroutine says 2 hello from thread ${Thread.currentThread().name}" )
        }

        Log.d(TAG, "Hello from thread ${Thread.currentThread().name}" )

        //If MAIN thread finishes their work, ALL other threads and coroutines will be canceled
        //Even if started on another thread
        //We can check that by increasing delay on the example above. If we destroy app before the delay ends, the print will not be displayed
        GlobalScope.launch {
            //Delay works different from a thread sleep because it will only pause the current coroutine and not pause the whole thread
            delay(5000)
            Log.d(TAG, "Coroutine says 3 hello from thread ${Thread.currentThread().name}" )
        }

    }


}