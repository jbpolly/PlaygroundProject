package com.mysticraccoon.playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mysticraccoon.playground.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.io.File

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        Log.d(TAG, "Before runBlocking")
//        runBlocking {
//            Log.d(TAG, "Start of runBlocking")
//            //starts a new coroutine in the main thread
//            delay(5000L)
//            Log.d(TAG, "End of runBlocking")
//        }
//        Log.d(TAG, "After runBlocking")


        Log.d(TAG, "Before runBlocking")
        runBlocking {
            launch(Dispatchers.IO) {
                //This one will actually run asynchronously
                delay(3000L)
                Log.d(TAG, "Finished IO coroutine 1")
            }

            launch(Dispatchers.IO) {
                //This one will actually run asynchronously
                delay(3000L)
                Log.d(TAG, "Finished IO coroutine 2")
            }

            Log.d(TAG, "Start of runBlocking")
            //starts a new coroutine in the main thread
            delay(5000L)
            Log.d(TAG, "End of runBlocking")
        }
        Log.d(TAG, "After runBlocking")

        //The difference is that the one below will not block the main thread. And the one above will
        GlobalScope.launch(Dispatchers.Main) {  }

        //The runblocking can be useful if you do not want coroutine behavior but still want to call a suspend fun on the main thread

        //Another functionality is for testing with JUnit
        //Use it to also quickly play around with coroutines


    }

}