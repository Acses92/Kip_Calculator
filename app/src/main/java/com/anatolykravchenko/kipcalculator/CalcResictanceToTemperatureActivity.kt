package com.anatolykravchenko.kipcalculator

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.anatolykravchenko.kipcalculator.databinding.RtdActivityBinding
import java.lang.Math.pow
import kotlin.NullPointerException
import kotlin.math.roundToLong
import kotlin.math.sqrt


class CalcResictanceToTemperatureActivity: AppCompatActivity() {
    private lateinit var binding: RtdActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.rtd_activity)
        binding = RtdActivityBinding.inflate(layoutInflater)

        val view = binding.root

        var resistance: Double = 0.0

        setContentView(view)

        val materialRTDSpinner = binding.RDTMaterialSpinner
        val material = resources.getStringArray(R.array.rtd_material)
   //     val materialTxtSelect = binding.outTestTexRTD


        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.rtd_material,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            materialRTDSpinner.adapter = adapter
        }

        materialRTDSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.outTestTextView.text = adapter.getItem(position)
                val position = adapter.getItemId(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        binding.resistanceEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

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

        binding.resistanceToTempButton.setOnClickListener {
            val temp = ptChoseListener(nominalResistance = 100.00, resistance)
            binding.outTestTextView.text = temp.toString()
        }
    }

    fun ptChoseListener(nominalResistance: Double, resistance: Double) : Double {
        if (resistance / nominalResistance < 1.0) {
            return converterPTtoTemperatureMinus(nominalResistance, resistance)
        } else {
            return converterPTtoTemperaturePlus(nominalResistance, resistance)
        }
    }

    fun converterPTtoTemperatureMinus(nominalResistance: Double, resistance: Double): Double {
        val d1 = 255.819
        val d2 = 9.14550
        val d3 = -2.92363
        val d4 = 1.79090

        val inputNam: Double = resistance/nominalResistance - 1.00

        return d1*inputNam+d2*pow(inputNam, 2.0)+ d3*pow(inputNam, 3.0)+d4*pow(inputNam, 4.0)
    }

    fun converterPTtoTemperaturePlus(nominalResistance: Double, resistance: Double): Double {
        val aCoefficient: Double = 3.9083e-3
        val bCoefficient: Double = -5.775e-7

        return (((kotlin.math.sqrt(pow(aCoefficient,2.0) - 4 * bCoefficient * (1 - resistance / nominalResistance)))
                - aCoefficient) /(2 * bCoefficient))
    }

    fun cubicRoot(number: Double) : Double {
        return (pow(number, 1/3.toDouble()))
    }

    fun fourthRoot(number: Double) : Double {
        return (pow(number, 1/4.toDouble()))
    }
}


