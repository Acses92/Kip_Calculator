package com.anatolykravchenko.kipcalculator.presentation.mainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anatolykravchenko.kipcalculator.presentation.currentLoop.CurrentLoopActivity
import com.anatolykravchenko.kipcalculator.databinding.ActivityMainBinding
import com.anatolykravchenko.kipcalculator.presentation.rtd.RtdActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.RtdSelectButton.setOnClickListener {
            val intent = Intent(this, RtdActivity::class.java)
            startActivity(intent)
        }

        binding.currentLoopSelectButton.setOnClickListener {
            val intent = Intent(this, CurrentLoopActivity::class.java)
            startActivity(intent)
        }
    }
}
