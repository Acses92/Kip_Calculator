package com.anatolykravchenko.kipcalculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.anatolykravchenko.kipcalculator.databinding.RtdActivityBinding

enum class SensorType {
    PlatinumPT, PlatinumP, Coopers
}

class RTDActivity: AppCompatActivity() {
    private lateinit var binding: RtdActivityBinding
    var nominalResistance: Double = 50.0
    lateinit var materialType: Enum<SensorType>
    var materialTypeString: String = ""
    var inputValue: Double = 0.0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.rtd_activity)
        binding = RtdActivityBinding.inflate(layoutInflater)

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
                    "Медь" -> {materialTypeString = SensorType.Coopers.toString()}
                    "Платина(PT)" -> {materialTypeString = SensorType.PlatinumPT.toString()}
                    "Платина(П)" -> {materialTypeString = SensorType.PlatinumP.toString()}
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
                nominalResistance = adapterNominalRes.getItem(position).toString().toDouble()
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
                    inputValue = 0.0
                    binding.rtdResultButton.isEnabled = false
                }
                else {
                    binding.rtdResultButton.isEnabled = true
                    inputValue = s.toString().toDouble()
                }
            }
        })

        //обрабатываем кнопку
        binding.rtdResultButton.setOnClickListener {
            if(binding.resToTempRadioButton.isChecked) {
                val temp = ResistanceToTemperature(nominalResistance, inputValue).
                sensorSelector(materialTypeString, nominalResistance, inputValue)
                "Значение температуры ${temp.toString().format(temp, )}".also { binding.outTestTextView.text = it }
            } else {
                val res = TemperatureToResistance(nominalResistance, inputValue).
                sensorSelector(materialTypeString, nominalResistance, inputValue)
                "Значение сопротивления ${res.toString().format(res, 3.00)}".also { binding.outTestTextView.text = it }
            }
        }
    }



}


