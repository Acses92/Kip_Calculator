package com.anatolykravchenko.kipcalculator.currentloop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anatolykravchenko.kipcalculator.R
import com.anatolykravchenko.kipcalculator.databinding.CurentLoopActivityBinding
import com.anatolykravchenko.kipcalculator.databinding.RtdActivityBinding

class CurrentLoopActivity:AppCompatActivity() {
    private lateinit var binding: CurentLoopActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CurentLoopActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}