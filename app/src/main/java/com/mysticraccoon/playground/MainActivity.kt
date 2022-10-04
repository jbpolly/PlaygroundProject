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

        //simpleJobJoin()
        //simpleJobCancellation()
        //intensiveJobCancellation()
        intensiveCalculationWithTimeout()


    }

    fun simpleJobJoin() {
        //When we launch a coroutine, it returns a job which we can save in a variable
        val job = GlobalScope.launch(Dispatchers.Default) {
            repeat(5) {
                Log.d(TAG, "Coroutine is still working")
                delay(1000)
            }
        }

        //now we can wait for it to finish
        runBlocking {
            job.join()
            Log.d(TAG, "Main thread is continuing...")
        }
    }

    fun simpleJobCancellation() {
        // we can also cancel it

        val job = GlobalScope.launch(Dispatchers.Default) {
            repeat(5) {
                Log.d(TAG, "Coroutine is still working")
                delay(1000)
            }
        }

        //now we can wait for it to finish
        runBlocking {
            //In this example we don't wait for the coroutine to join
            //we just cancel it after 2 seconds
            //so we will not see the 5 repeats of the code executed above
            delay(2000L)
            job.cancel()
            Log.d(TAG, "Main thread is continuing...")
        }
    }

    fun intensiveJobCancellation() {
        //======================================================
        //cancelling a coroutine may not be always as easy as in the example above
        //this is because cancellation is cooperative
        //it needs to be enough time to tell the coroutine it has been cancelled
        //in the example below there was lots of delays which pauses the coroutine and makes it easier to signalize to it that the coroutine has been cancelled
        //but if it had an intensive computation it would not be as easy
        //check the example below

        val job = GlobalScope.launch(Dispatchers.Default) {
            Log.d(TAG, "Starting long running calculation")
            for (i in 30..43) {
                //we need to check manually to see if coroutine is still active
                if(isActive){
                    Log.d(TAG, "Result for i = $i: ${fib(i)}")
                }
            }
            Log.d(TAG, "Ending long running calculation")

        }

        runBlocking {
            delay(2000L)
            job.cancel()
            Log.d(TAG, "Canceled job!")
        }

    }

    fun intensiveCalculationWithTimeout(){
        //A real life situation where we may want to cancel a coroutine is a timeout for example
        //we can use the withTimeout function for that
        val job = GlobalScope.launch(Dispatchers.Default) {
            Log.d(TAG, "Starting long running calculation")
            withTimeout(2000L){
                for (i in 30..43) {
                    //we need to check manually to see if coroutine is still active
                    if(isActive){
                        Log.d(TAG, "Result for i = $i: ${fib(i)}")
                    }
                }
            }
            Log.d(TAG, "Ending long running calculation")
        }
        //we do not need to cancel manually anymore

    }

    fun fib(n: Int): Long {
        return if (n == 0) 0
        else if (n == 1) 1
        else fib(n - 1) + fib(n - 2)
    }

}