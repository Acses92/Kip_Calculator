package com.anatolykravchenko.kipcalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anatolykravchenko.kipcalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.curentLoopChcoiseButton.setOnClickListener {

        }

        binding.rtdChoiceButton.setOnClickListener {
            val intent = Intent(this, CalcResictanceToTemperatureActivity::class.java)
            startActivity(intent)

        }

    }
}