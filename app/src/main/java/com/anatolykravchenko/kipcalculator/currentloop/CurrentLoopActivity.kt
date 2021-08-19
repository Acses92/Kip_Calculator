package com.anatolykravchenko.kipcalculator.currentloop

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        binding.upermCurrentLevelEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }
        })


    }
}