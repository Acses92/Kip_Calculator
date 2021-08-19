package com.anatolykravchenko.kipcalculator.currentloop

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anatolykravchenko.kipcalculator.R
import com.anatolykravchenko.kipcalculator.databinding.CurentLoopActivityBinding
import com.anatolykravchenko.kipcalculator.databinding.RtdActivityBinding

class CurrentLoopActivity:AppCompatActivity() {
    private lateinit var binding: CurentLoopActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CurentLoopActivityBinding.inflate(layoutInflater)
        val view = binding.root
        val currentLoopVM = ViewModelProvider(this).get(CurrentLoopViewModel::class.java)
        setContentView(view)

        binding.upermCurrentLevelEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty() || s.contentEquals(".")) {
                    currentLoopVM.highLimit = 0.0
                }else currentLoopVM.highLimit = s.toString().toDouble()
            }
        })

        binding.lowCurrentLevelEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty() || s.contentEquals(".")) {
                    currentLoopVM.lowLimit = 0.0
                }else currentLoopVM.lowLimit = s.toString().toDouble()
            }
        })

        binding.currentValueEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty() || s.contentEquals(".")) {
                    currentLoopVM.value = 0.0
                }else currentLoopVM.value = s.toString().toDouble()
            }
        })


        binding.currentResultButton.setOnClickListener {

        }

    }
    fun inputValueChecker(lowLimit: Double, highLimit: Double, value: Double) {
        if(lowLimit>highLimit) {
            Toast.makeText(applicationContext, "Нижнее значение не может быть больше верхнего",
                Toast.LENGTH_SHORT).show()
        }

        if(value>highLimit || value <lowLimit) {
            Toast.makeText(applicationContext, "Вы ввели значение вне пределов",
                Toast.LENGTH_SHORT).show()
        }
    }

}

