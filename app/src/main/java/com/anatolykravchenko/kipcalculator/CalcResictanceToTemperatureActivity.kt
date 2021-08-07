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


class CalcResictanceToTemperatureActivity: AppCompatActivity() {
    private lateinit var binding: RtdActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.rtd_activity)
        binding = RtdActivityBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)


        var nominalResistance: Double = 1000.0
        var materialSelected: Long = 0
        var resistance: Double = 0.0
        val materialRTDSpinner = binding.RDTMaterialSpinner
        val nominalResSpinner = binding.rtdResistanceSpinner


        val adapterRTD = ArrayAdapter.createFromResource(
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
              //  binding.outTestTextView.text = adapter.getItem(position)
                materialSelected = adapterRTD.getItemId(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val adapterNominalRes = ArrayAdapter.createFromResource(this,
            R.array.rtd_resistance,
        android.R.layout.simple_dropdown_item_1line
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            nominalResSpinner.adapter = adapter
        }

        nominalResSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
             //   nominalResistance = adapterRTD.getItem(position).toString().toDouble()
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
            val temp = ptChoseListener(nominalResistance, resistance)
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


