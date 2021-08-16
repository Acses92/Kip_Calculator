package com.anatolykravchenko.kipcalculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anatolykravchenko.kipcalculator.databinding.RtdActivityBinding
import com.anatolykravchenko.kipcalculator.rtd.RTDVM
import com.anatolykravchenko.kipcalculator.rtd.resistancetotemperautre.ResistanceToTemperatureSelector
import com.anatolykravchenko.kipcalculator.rtd.temperaturetoresistance.TemperatureToResistanceSelector
import java.lang.Exception
import java.math.RoundingMode

enum class SensorType {
    PlatinumPT, PlatinumP, Coopers
}

class RTDActivity: AppCompatActivity() {
    private lateinit var binding: RtdActivityBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.rtd_activity)
        binding = RtdActivityBinding.inflate(layoutInflater)
        //иницициализиреум вью модель
        val RTDViewModel = ViewModelProvider(this).get(RTDVM::class.java)
        val view = binding.root
        setContentView(view)

        val materialRTDSpinner = binding.RDTMaterialSpinner
        val nominalResSpinner = binding.rtdResistanceSpinner

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
                    "Медь" -> {RTDViewModel.materialTypeString =
                        SensorType.Coopers.toString()}
                    "Платина(PT)" -> {RTDViewModel.materialTypeString =
                        SensorType.PlatinumPT.toString()}
                    "Платина(П)" -> {RTDViewModel.materialTypeString =
                        SensorType.PlatinumP.toString()}
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
                RTDViewModel.nominalResistance = adapterNominalRes.getItem(position).toString().toDouble()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //обрабатывае ввод сопротивления
        binding.rtdEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.contentEquals(""))
                {
                    RTDViewModel.inputValue = 0.0
                    binding.rtdResultButton.isEnabled = false
                }
                else {
                    binding.rtdResultButton.isEnabled = true
                    RTDViewModel.inputValue = s.toString().toDouble()
                }
            }
        })

        //обрабатываем кнопку получить значение
        binding.rtdResultButton.setOnClickListener {
            if(binding.resToTempRadioButton.isChecked) {
                try {
                    val temp = RTDViewModel.getTemperature(RTDViewModel.materialTypeString,
                        RTDViewModel.nominalResistance,
                        RTDViewModel.inputValue).toBigDecimal().setScale(3,RoundingMode.UP)
                    "Значение температуры $temp".also { binding.outTestTextView.text = it }
                } catch(e: Exception) {
                    binding.outTestTextView.text = "Введено некоректное значение" }
            } else {
                try {
                    val res = RTDViewModel.getResistance(RTDViewModel.materialTypeString,
                        RTDViewModel.nominalResistance,
                        RTDViewModel.inputValue).toBigDecimal().setScale(3, RoundingMode.UP)

                    "Значение сопротивления $res".also { binding.outTestTextView.text = it }
                } catch (e: Exception) {
                    binding.outTestTextView.text = "Введено некоректное значение" }
            }

        }
    }
}


