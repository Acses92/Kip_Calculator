package com.anatolykravchenko.kipcalculator.currentloop

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anatolykravchenko.kipcalculator.databinding.CurentLoopActivityBinding
import java.lang.Exception
import java.math.RoundingMode

class CurrentLoopActivity:AppCompatActivity() {
    private lateinit var binding: CurentLoopActivityBinding


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CurentLoopActivityBinding.inflate(layoutInflater)
        val view = binding.root
        val currentLoopVM = ViewModelProvider(this).get(CurrentLoopViewModel::class.java)
        setContentView(view)


        //Обрабатываем ввод верхего значения токовой петли
        binding.upermCurrentLevelEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty() || s.contentEquals(".")) {
                    currentLoopVM.highLimit = 0.0
                }else currentLoopVM.highLimit = s.toString().toDouble()
            }
        })

        //Обрабатываем ввод нижнего значения токовой петли
        binding.lowCurrentLevelEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty() || s.contentEquals(".")) {
                    currentLoopVM.lowLimit = 0.0
                }else currentLoopVM.lowLimit = s.toString().toDouble()
            }
        })

        //Обрабатываем значение вводимой величины
        binding.currentValueEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.currentResultButton.isEnabled = true
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.isNullOrEmpty() || s.contentEquals(".")) {
                    currentLoopVM.value = 0.0
                }else currentLoopVM.value = s.toString().toDouble()
            }
        })

        //обрабатываем Radio Button. Это не красиво, но таков путь. false - расчитываем ток
        //true - расчитываем измеряемую величину
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if(binding.currentRadioButton.isChecked){
                currentLoopVM.currentOperationType = false
            }
            if(binding.valueRadioButton.isChecked) {
                currentLoopVM.currentOperationType = true }
        }

        //обрабатываем нажатие кнопки
        binding.currentResultButton.setOnClickListener {
            if(inputValueChecker(currentLoopVM.lowLimit, currentLoopVM.highLimit,
                    currentLoopVM.value, currentLoopVM.currentOperationType))
            {
                if(binding.valueRadioButton.isChecked) {
                    try {
                        val value = currentLoopVM.getValue(currentLoopVM.lowLimit,
                            currentLoopVM.highLimit, currentLoopVM.value).toBigDecimal().
                        setScale(3, RoundingMode.UP)
                        "Значение измееряемой величины $value".also {
                            binding.currentResultEditText.text = it }
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, "Что-то пошло не так"
                            , Toast.LENGTH_LONG).show()
                    }
                } else {
                    try {
                        val current = currentLoopVM.getCurrent(currentLoopVM.lowLimit,
                            currentLoopVM.highLimit, currentLoopVM.value).
                           toBigDecimal().setScale(3, RoundingMode.UP)
                        "Значение тока $current".also { binding.currentResultEditText.text = it }
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext,
                            "Что-то пошло не так", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
    //Функция проверяет корректность введенных значений
    private fun inputValueChecker(lowLimit: Double, highLimit: Double, value: Double,
                                  currentOperationType: Boolean): Boolean {
        if(lowLimit.isInfinite() || highLimit.isInfinite() || value.isInfinite()) {
            Toast.makeText(applicationContext, "Введены не все значения",
                Toast.LENGTH_SHORT).show()
            return false
        }

        if(lowLimit>highLimit) {
            Toast.makeText(applicationContext, "Нижнее значение не может быть больше верхнего",
                Toast.LENGTH_SHORT).show()
            return false
        }

        if((value>highLimit || value<lowLimit) && !currentOperationType) {
            Toast.makeText(applicationContext, "Вы ввели значение вне пределов",
                Toast.LENGTH_SHORT).show()
            return false
        }

        if(currentOperationType && (value<4.0 || value>20.0)) {
          Toast.makeText(applicationContext, "Вы ввели некоректное значение тока",
          Toast.LENGTH_SHORT).show()
            return false
        }

        if(highLimit.equals(0.00)) {
            Toast.makeText(applicationContext, "Введено нулевое значение верхней границы",
                Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}

