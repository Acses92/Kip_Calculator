package com.anatolykravchenko.kipcalculator.presentation.mainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anatolykravchenko.kipcalculator.presentation.currentLoop.CurrentLoopActivity
import com.anatolykravchenko.kipcalculator.databinding.ActivityMainBinding
import com.anatolykravchenko.kipcalculator.presentation.rtd.RtdActivity
import com.anatolykravchenko.kipcalculator.R

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::bind, R.id.main_container)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
