package com.anatolykravchenko.kipcalculator

import android.annotation.SuppressLint
import android.icu.util.UniversalTimeScale.toBigDecimal
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import java.math.BigDecimal
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
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

        val resultObserver = Observer<String> {
            binding.outTestTextView.text = it
        }
        RTDViewModel.resultString.observe(this, resultObserver)

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

        //обрабатывае ввод сопротивления. Переделать в лямбду
        binding.rtdEditText.doOnTextChanged { text, _, _, _ ->
            if(text.contentEquals(".")|| text.isNullOrEmpty())
            {
                RTDViewModel.inputValue = 0.0
                binding.rtdResultButton.isEnabled =false
            }else {
                RTDViewModel.inputValue = text.toString().toDouble()
                binding.rtdResultButton.isEnabled = true
            }
        }

        binding.rtdRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if(binding.resToTempRadioButton.isChecked) {
                RTDViewModel.operationType = RTDVM.OperationType.Temperature
            }
            if(binding.tempToResistRadioButton.isChecked) {
                RTDViewModel.operationType = RTDVM.OperationType.Value
            }
        }

        //обрабатываем кнопку получить значение
        binding.rtdResultButton.setOnClickListener {
            RTDViewModel.getResult()
        }


    }




        fun resultChecker(materialType: RTDVM.SensorType, result: Double): Boolean {
        if(result>850.00 && (materialType==RTDVM.SensorType.PlatinumP
                    || materialType==RTDVM.SensorType.PlatinumP)) {
            Toast.makeText(applicationContext, "Сопротивление вне пределов выбранного датчика",
                Toast.LENGTH_SHORT).show()
            return false
        }

        if((result<-180.0 || result>200) && materialType == RTDVM.SensorType.Coopers )
        {
            Toast.makeText(applicationContext, "Сопротивление вне пределов выбранного датчика",
            Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


    fun inputChecker(materialType: RTDVM.SensorType, inputTemperature: Double): Boolean {
        if( (materialType ==RTDVM.SensorType.Coopers) && inputTemperature>200.0) {
           Toast.makeText(applicationContext, "Введенная температура" +
                    " вне пределов работы данного датчика", Toast.LENGTH_SHORT).show()
            return false
        }
        if((materialType ==RTDVM.SensorType.PlatinumP ||
            materialType==RTDVM.SensorType.PlatinumPT) && inputTemperature>850.0) {
            Toast.makeText(applicationContext, "Введенная температура" +
                    " вне пределов работы данного датчика",
            Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}


