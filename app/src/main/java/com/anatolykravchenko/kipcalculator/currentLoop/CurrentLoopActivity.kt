package com.anatolykravchenko.kipcalculator.currentLoop

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
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

        val resultObserver = Observer<String> {
            binding.currentResultEditText.text = it
        }
        currentLoopVM.result.observe(this, resultObserver)

        currentLoopVM.message.observe(this) {errorType ->
            showMessage(getString(errorType.getString()))
        }

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

            currentLoopVM.buttonClicker()
        }

    }
    fun showMessage(message: String) {
        applicationContext?.let {
            Toast.makeText(it, message,Toast.LENGTH_SHORT )
                .show()
        }
    }

    private fun CurrentLoopErrorType.getString(): Int =
        when(this) {
            CurrentLoopErrorType.WRONG_CURRENT_LIMITS -> R.string.wrong_current_limits
            CurrentLoopErrorType.LOW_LIMIT_MORE_HIGH_LIMIT -> R.string.low_limit_more_high_limit
            CurrentLoopErrorType.WRONG_VALUE_LIMITS -> R.string.wrong_value_limit
            CurrentLoopErrorType.WRONG_HIGH_LIMIT -> R.string.wrong_high_limit
        }
}

