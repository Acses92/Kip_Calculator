package com.anatolykravchenko.kipcalculator.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.anatolykravchenko.kipcalculator.R
import com.anatolykravchenko.kipcalculator.databinding.RtdActivityBinding


class RtdActivity: AppCompatActivity() {
    private lateinit var binding: RtdActivityBinding
    val RtdViewModel by viewModels<RtdVM>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.rtd_activity)
        binding = RtdActivityBinding.inflate(layoutInflater)
        //иницициализиреум вью модель
    //    val RtdViewModel = ViewModelProvider(this).get(RtdVM::class.java)
        val view = binding.root
        setContentView(view)
        val materialRTDSpinner = binding.RDTMaterialSpinner
        val nominalResSpinner = binding.rtdResistanceSpinner
        RtdViewModel.message.observe(this) {errorType->
            showMessage(getString(errorType.getString()))
        }

        val resultObserver = Observer<String> {
            binding.outTestTextView.text = it
        }
        RtdViewModel.resultString.observe(this, resultObserver)

        //Инициализирум массив материалов датчиков
        val adapterRTD = ArrayAdapter.createFromResource(
            this,
            R.array.rtd_material,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            materialRTDSpinner.adapter = adapter
        }

        //Обработка выбор материала датчика
        materialRTDSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //Обрабатываем материал датчтика.
                when(adapterRTD.getItem(position).toString()) {
                    "Медь" -> {RtdViewModel.materialType =
                        RtdVM.SensorType.Coopers}
                    "Платина(PT)" -> {RtdViewModel.materialType =
                        RtdVM.SensorType.PlatinumPT}
                    "Платина(П)" -> {RtdViewModel.materialType =
                        RtdVM.SensorType.PlatinumP}
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //Инициализирум массив номинальных сопротивлений
        val adapterNominalRes = ArrayAdapter.createFromResource(this,
            R.array.rtd_resistance,
        android.R.layout.simple_dropdown_item_1line
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            nominalResSpinner.adapter = adapter
        }

        //Обрабатываем выбор номинального сопротивления
        nominalResSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                RtdViewModel.nominalResistance = adapterNominalRes.getItem(position).toString().toDouble()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //обрабатывае ввод сопротивления. Переделать в лямбду
        binding.rtdEditText.doOnTextChanged { text, _, _, _ ->
            if(text.contentEquals(".")|| text.isNullOrEmpty())
            {
                RtdViewModel.inputValue = 0.0
                binding.rtdResultButton.isEnabled =false
            }else {
                RtdViewModel.inputValue = text.toString().toDouble()
                binding.rtdResultButton.isEnabled = true
            }
        }

        binding.rtdRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if(binding.resToTempRadioButton.isChecked) {
                RtdViewModel.operationType = RtdVM.OperationType.Temperature
            }
            if(binding.tempToResistRadioButton.isChecked) {
                RtdViewModel.operationType = RtdVM.OperationType.Value
            }
        }
        //обрабатываем кнопку получить значение
        binding.rtdResultButton.setOnClickListener {
            RtdViewModel.getResult()
        }
    }

    fun showMessage(message: String) {
        applicationContext?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT )
                .show()
        }
    }
    private fun RTDErrorType.getString(): Int =
        when(this) {
            RTDErrorType.WRONG_TEMPERATURE_LIMIT -> R.string.wrong_temperature_limit
            RTDErrorType.WRONG_RESISTANCE_LIMIT -> R.string.wrong_resistance_limit
    }

}


