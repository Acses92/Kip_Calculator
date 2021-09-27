package com.anatolykravchenko.kipcalculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.anatolykravchenko.kipcalculator.databinding.RtdActivityBinding
import com.anatolykravchenko.kipcalculator.rtd.RTDVM
import java.lang.Exception
import java.math.RoundingMode



class RTDActivity: AppCompatActivity() {
    private lateinit var binding: RtdActivityBinding
 //   val RTDViewModel by viewModels<RTDVM>()

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
                    "Медь" -> {RTDViewModel.materialType =
                        RTDVM.SensorType.Coopers}
                    "Платина(PT)" -> {RTDViewModel.materialType =
                        RTDVM.SensorType.PlatinumPT}
                    "Платина(П)" -> {RTDViewModel.materialType =
                        RTDVM.SensorType.PlatinumP}
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
                if(s.contentEquals(".") || s.isNullOrEmpty())
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
                    val temp = RTDViewModel.getTemperature(
                        RTDViewModel.nominalResistance,
                        RTDViewModel.inputValue)
                    if(resultChecker(RTDViewModel.materialTypeString, temp)) {
                        "Значение температуры ${temp.toBigDecimal().setScale(3, 
                            RoundingMode.UP)}".also { binding.outTestTextView.text = it }
                    }
                } catch(e: Exception) {
                    Toast.makeText(applicationContext, "Получено некоректное значение",
                        Toast.LENGTH_SHORT).show() }
            } else {
                try {
                    if (inputChecker(RTDViewModel.materialTypeString, RTDViewModel.inputValue)) {
                        val res = RTDViewModel.getResistance(
                            RTDViewModel.nominalResistance,
                            RTDViewModel.inputValue)

                        "Значение сопротивления ${res.toBigDecimal().setScale(3, 
                            RoundingMode.UP)}".also { binding.outTestTextView.text = it }
                    }

                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Получено некоректное значение",
                        Toast.LENGTH_SHORT).show() }
            }

        }

    }

    private fun resultChecker(materialTypeString: String, result: Double): Boolean {
        if(result>850.00 && (materialTypeString=="PlatinumP" ||materialTypeString=="PlatinumPT")) {
            Toast.makeText(applicationContext, "Сопротивление вне пределов выбранного датчика",
                Toast.LENGTH_SHORT).show()
            return false
        }

        if((result<-180.0 || result>200) && materialTypeString=="Coopers" )
        {
            Toast.makeText(applicationContext, "Сопротивление вне пределов выбранного датчика",
            Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun inputChecker(materialTypeString: String, inputTemperature: Double): Boolean {
        if( (materialTypeString =="Coopers") && inputTemperature>200.0) {
           Toast.makeText(applicationContext, "Введенная температура" +
                    " вне пределов работы данного датчика", Toast.LENGTH_SHORT).show()
            return false
        }
        if((materialTypeString =="PlatinumP" ||
            materialTypeString =="PlatinumPT") && inputTemperature>850.0) {
            Toast.makeText(applicationContext, "Введенная температура" +
                    " вне пределов работы данного датчика",
            Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}


