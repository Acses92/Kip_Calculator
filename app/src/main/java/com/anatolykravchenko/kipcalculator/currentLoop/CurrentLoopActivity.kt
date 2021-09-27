package com.anatolykravchenko.kipcalculator.currentLoop

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.anatolykravchenko.kipcalculator.R
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anatolykravchenko.kipcalculator.databinding.CurentLoopActivityBinding
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode

class CurrentLoopActivity:AppCompatActivity(R.layout.curent_loop_activity) {

    private lateinit var binding: CurentLoopActivityBinding

    ///С делегатом какая-то фигня. Во фрагмнетах всё работает. В активити нет.
  //  private val binding by viewBinding(CurentLoopActivityBinding:: bind, R.id.container)
   // private val binding: CurentLoopActivityBinding by viewBinding (R.id.container)
    val currentLoopVM by viewModels<CurrentLoopViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CurentLoopActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Обрабатываем ввод верхего значения токовой петли
        binding.upermCurrentLevelEditText.doOnTextChanged { text, _, _, _ ->
            if(text.isNullOrEmpty() || text.contentEquals(".")) {
                currentLoopVM.highLimit = 0.0
            }else currentLoopVM.highLimit = text.toString().toDouble()
        }

        //Обрабатываем ввод нижнего значения токовой петли
        binding.lowCurrentLevelEditText.doOnTextChanged { text, _, _, _ ->
            if(text.isNullOrEmpty() || text.contentEquals(".")) {
                currentLoopVM.lowLimit = 0.0
            }else currentLoopVM.lowLimit = text.toString().toDouble()
        }

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

        //обрабатываем Radio Button.
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if(binding.currentRadioButton.isChecked){
                currentLoopVM.currentOperationType = OperationType.Current
            }
            if(binding.valueRadioButton.isChecked) {
                currentLoopVM.currentOperationType = OperationType.Value }
        }

        //обрабатываем нажатие кнопки
        binding.currentResultButton.setOnClickListener {
            if(inputValueChecker())
            {
                val result = currentLoopVM.buttonClicker()
                if(result is BigDecimal) {
                    if(currentLoopVM.currentOperationType==OperationType.Current)
                    {
                        "Значение тока равно $result".also {
                            binding.currentResultEditText.text = it }
                    }
                    else {
                        "Значение величины равно $result".also {
                            binding.currentResultEditText.text = it }
                    }
                } else {
                    "Что-то пошло не так".also {
                        binding.currentResultEditText.text = it
                    }
                }

            }
        }
    }
    //Функция проверяет корректность введенных значений
     fun inputValueChecker(): Boolean {
        if(currentLoopVM.lowLimit.isInfinite() || currentLoopVM.highLimit.isInfinite() ||
            currentLoopVM.value.isInfinite()) {
            Toast.makeText(applicationContext, "Введены не все значения",
                Toast.LENGTH_SHORT).show()
            return false
        }

        if(currentLoopVM.lowLimit>currentLoopVM.highLimit) {
            Toast.makeText(applicationContext, "Нижнее значение не может быть больше верхнего",
                Toast.LENGTH_SHORT).show()
            return false
        }

        if((currentLoopVM.value>currentLoopVM.highLimit ||
                    currentLoopVM.value<currentLoopVM.lowLimit)
            && currentLoopVM.currentOperationType == OperationType.Value) {
            Toast.makeText(applicationContext, "Вы ввели значение вне пределов",
                Toast.LENGTH_SHORT).show()
            return false
        }

        if(currentLoopVM.currentOperationType == OperationType.Current &&
            (currentLoopVM.value<4.0 || currentLoopVM.value>20.0))
            {
          Toast.makeText(applicationContext, "Вы ввели некоректное значение тока",
          Toast.LENGTH_SHORT).show()
            return false
            }

        if(currentLoopVM.highLimit.equals(0.00)) {
            Toast.makeText(applicationContext, "Введено нулевое значение верхней границы",
                Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}

