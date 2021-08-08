package com.anatolykravchenko.kipcalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.anatolykravchenko.kipcalculator.databinding.RtdActivityBinding
import java.lang.Math.pow
import android.content.pm.ActivityInfo


class CalcResistanceToTemperatureActivity: AppCompatActivity() {
    private lateinit var binding: RtdActivityBinding


    var nominalResistance: Double = 50.0
    var materialSelected: Long = 0
    var resistance: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {

     //   requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
              //  binding.outTestTextView.text = adapter.getItem(position)
                materialSelected = adapterRTD.getItemId(position)
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
        binding.resistanceEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.contentEquals(""))
                {
                    resistance = 0.0
                    binding.resistanceToTempButton.isEnabled = false
                }
                else {
                    binding.resistanceToTempButton.isEnabled = true
                    resistance = s.toString().toDouble()
                }
            }
        })
        //обрабатываем кнопку
        binding.resistanceToTempButton.setOnClickListener {
            val temp = ResistanceToTemperature.PlatinaSensor(nominalResistance, resistance)
                .getOperationType(nominalResistance, resistance)
            binding.outTestTextView.text = temp.toString()
        }
    }



}


