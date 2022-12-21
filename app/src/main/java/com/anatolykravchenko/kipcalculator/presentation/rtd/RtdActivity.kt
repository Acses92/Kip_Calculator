package com.anatolykravchenko.kipcalculator.presentation.rtd

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.anatolykravchenko.kipcalculator.R
import com.anatolykravchenko.kipcalculator.databinding.RtdActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RtdActivity : AppCompatActivity() {

    private val binding by viewBinding(
        RtdActivityBinding::bind,
        R.id.rtd_container
    )
    val rtdViewModel by viewModels<RtdViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rtd_activity)
        val view = binding.root
        setContentView(view)
        viewModelObserver()
        rtdInputListener()
        adapterInit()

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun adapterInit() {
        val materialRTDSpinner = binding.RDTMaterialSpinner
        val nominalResSpinner = binding.rtdResistanceSpinner

        //инициализируем массив для материалов датчика
        val adapterRTD = ArrayAdapter.createFromResource(
            this,
            R.array.rtd_material,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            materialRTDSpinner.adapter = adapter
        }

        //инициализируем массив для сопротивлений
        val adapterNominalRes = ArrayAdapter.createFromResource(
            this,
            R.array.rtd_resistance,
            android.R.layout.simple_dropdown_item_1line
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            nominalResSpinner.adapter = adapter
        }
        //Обрабатываем выбор номинального сопротивления
        nominalResSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                rtdViewModel.nominalResistance =
                    adapterNominalRes.getItem(position).toString().toDouble()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //обрабатывае выбор типа датчика
        materialRTDSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //Обрабатываем материал датчтика.
                when (adapterRTD.getItem(position).toString()) {
                    "Медь" -> {
                        rtdViewModel.materialType =
                            SensorType.Coopers
                    }
                    "Платина(PT)" -> {
                        rtdViewModel.materialType =
                            SensorType.PlatinumPT
                    }
                    "Платина(П)" -> {
                        rtdViewModel.materialType =
                            SensorType.PlatinumP
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun viewModelObserver() {
        rtdViewModel.message.observe(this) { errorType ->
            showMessage(getString(errorType.getString()))
        }

        val resultObserver = Observer<String> {
            binding.outTestTextView.text = it
        }
        rtdViewModel.resultString.observe(this, resultObserver)
    }

    private fun rtdInputListener() {
        binding.rtdEditText.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty() && text.isDigitsOnly()) {
                rtdViewModel.inputValue = text.toString().toDouble()
                binding.rtdResultButton.isEnabled = true
            } else {
                rtdViewModel.inputValue = 0.00
                binding.rtdResultButton.isEnabled = true
            }
        }

        binding.rtdRadioGroup.setOnCheckedChangeListener { _, _ ->
            if (binding.resToTempRadioButton.isChecked) {
                rtdViewModel.operationType = OperationType.Temperature
                binding.rtdEditText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
                binding.rtdEditText.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED
            }
            if (binding.tempToResistRadioButton.isChecked) {
                rtdViewModel.operationType = OperationType.Value
                binding.rtdEditText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            }
        }
        //обрабатываем кнопку получить значение
        binding.rtdResultButton.setOnClickListener {
            rtdViewModel.getResult()
        }
    }

    private fun showMessage(message: String) {
        applicationContext?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun RTDErrorType.getString(): Int =
        when (this) {
            RTDErrorType.WRONG_TEMPERATURE_LIMIT -> R.string.wrong_temperature_limit
            RTDErrorType.WRONG_RESISTANCE_LIMIT -> R.string.wrong_resistance_limit
        }

}


