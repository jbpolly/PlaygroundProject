package com.mysticraccoon.playground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
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

        //two very used pre defined scopes
        //viewModelScope and lifecycleScope

       // launchAnotherActivityLeak()
        launchAnotherActivityScoped()
    }

    fun launchAnotherActivityLeak(){
        binding.btnStartActivity.setOnClickListener {
            GlobalScope.launch {
                while (true){
                    delay(1000L)
                    Log.d(TAG, "Still running")
                }
            }
            GlobalScope.launch {
                delay(5000L)
                Intent(this@MainActivity, SecondActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }

    fun launchAnotherActivityScoped(){
        binding.btnStartActivity.setOnClickListener {
            //this stick the coroutine to this activity lifecycle
            //so when this activity is destroyed it will also get cancelled
            lifecycleScope.launch {
                while (true){
                    delay(1000L)
                    Log.d(TAG, "Still running")
                }
            }
            GlobalScope.launch {
                delay(5000L)
                Intent(this@MainActivity, SecondActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }



}