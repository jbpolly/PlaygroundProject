package com.mysticraccoon.playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mysticraccoon.playground.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    val TAG = "SecondActivity"

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}