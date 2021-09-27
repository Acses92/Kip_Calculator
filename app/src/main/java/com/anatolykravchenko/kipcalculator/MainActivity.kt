package com.anatolykravchenko.kipcalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anatolykravchenko.kipcalculator.currentLoop.CurrentLoopActivity
import com.anatolykravchenko.kipcalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.RtdSelectButton.setOnClickListener {
            val intent = Intent(this, RTDActivity::class.java)
            startActivity(intent)
        }

        binding.currentLoopSelectButton.setOnClickListener {
            val intent = Intent(this, CurrentLoopActivity::class.java)
            startActivity(intent)
        }

    }
}