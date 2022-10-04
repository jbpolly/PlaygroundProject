package com.mysticraccoon.playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mysticraccoon.playground.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.io.File
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //executeCallsSequential()
        //executeCallsParallel()
        //executeCallsParallelWithAsync()
        executeParallelWithWaitAll()

    }

    fun executeParallelWithWaitAll(){
        GlobalScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Executing parent coroutine on thread ${Thread.currentThread().name}")
            val time = measureTimeMillis {
                val deferreds = listOf(     // fetch two docs at the same time
                    async { networkCall1() },  // async returns a result for the first doc
                    async { networkCall2() }   // async returns a result for the second doc
                )
                val answers = deferreds.awaitAll()
                Log.d(TAG, "Answers are: ${answers.joinToString("|")}")

            }
            //It will take 6 seconds until the strings are displayed on this case
            //Because they will be sequential
            Log.d(TAG, "Requests tooke $time ms")
        }
    }

    fun executeCallsParallelWithAsync(){
        //async returns a deferred instead of a job
        GlobalScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Executing parent coroutine on thread ${Thread.currentThread().name}")
            val time = measureTimeMillis {
                var answer1 = async { networkCall1() }
                var answer2 = async { networkCall2() }
                Log.d(TAG, "Answer 1 is ${answer1.await()}")
                Log.d(TAG, "Answer 2 is ${answer2.await()}")
            }
            //It will take 6 seconds until the strings are displayed on this case
            //Because they will be sequential
            Log.d(TAG, "Requests tooke $time ms")
        }
    }

    fun executeCallsParallel(){
        GlobalScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Executing parent coroutine on thread ${Thread.currentThread().name}")
            val time = measureTimeMillis {
                var answer1: String? = null
                var answer2: String? = null
                val job1 = launch { answer1 = networkCall1() }
                val job2 = launch { answer2 = networkCall2() }
                //join to wait for answers or we would print null values
                job1.join()
                job2.join()
                Log.d(TAG, "Answer 1 is $answer1")
                Log.d(TAG, "Answer 2 is $answer2")
            }
            //It will take 6 seconds until the strings are displayed on this case
            //Because they will be sequential
            Log.d(TAG, "Requests tooke $time ms")
        }
    }

    fun executeCallsSequential(){
        //is we have several suspend functions and execute them in the same coroutine they are sequential by default
        //if we want to make them parallel we can use async or launches inside
        GlobalScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Executing parent coroutine on thread ${Thread.currentThread().name}")
            val time = measureTimeMillis {
                val answer1 = networkCall1()
                val answer2 = networkCall2()
                Log.d(TAG, "Answer 1 is $answer1")
                Log.d(TAG, "Answer 2 is $answer2")
            }
            //It will take 6 seconds until the strings are displayed on this case
            //Because they will be sequential
            Log.d(TAG, "Requests tooke $time ms")
        }
    }

    suspend fun networkCall1(): String{
        Log.d(TAG, "Executing network call 1 on thread ${Thread.currentThread().name}")
        delay(3000L)
        return "Answer 1"
    }

    suspend fun networkCall2(): String{
        Log.d(TAG, "Executing network call 2 on thread ${Thread.currentThread().name}")
        delay(3000L)
        return "Answer 2"
    }

}