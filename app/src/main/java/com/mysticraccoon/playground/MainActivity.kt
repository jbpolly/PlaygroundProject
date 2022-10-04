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


        //Depending on what our coroutine should do, we should pass a different dispatcher
        GlobalScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Starting coroutine on thread ${Thread.currentThread().name}")
            //we can also create our new context
            //newSingleThreadContext("MyThread")

            //but the most useful is that we can switch between coroutine contexts
            val answer = doNetworkCall()
            withContext(Dispatchers.Main){
                Log.d(TAG, "Setting text on thread ${Thread.currentThread().name}")
                binding.dummyText.text = answer
            }
        }

    }

    suspend fun doNetworkCall(): String{
        delay(3000L)
        return "This is the answer"
    }

}