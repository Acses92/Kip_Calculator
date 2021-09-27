package com.anatolykravchenko.kipcalculator.rtd

import androidx.lifecycle.ViewModel
import com.anatolykravchenko.kipcalculator.SensorType
import com.anatolykravchenko.kipcalculator.rtd.resistancetotemperautre.ResistanceToTemperatureSelector
import com.anatolykravchenko.kipcalculator.rtd.temperatureToResistance.TemperatureToResistanceSelector


class RTDVM: ViewModel() {

    var nominalResistance: Double = 50.0
    var inputValue: Double = 0.0
    var materialTypeString: String = ""
    lateinit var materialType: Enum<SensorType>
    var temperature: Double = 0.00
    var resistance: Double = 0.00

    fun getTemperature(materialTypeString: String, nominalResistance: Double, inputValue:Double)
    :Double  {
       return ResistanceToTemperatureSelector().sensorSelector(materialTypeString,
           nominalResistance, inputValue)
           .also { temperature = it }
    }

    fun getResistance(materialTypeString: String, nominalResistance: Double, inputValue:Double)
    :Double  {
        return TemperatureToResistanceSelector().sensorSelector(materialTypeString,
            nominalResistance, inputValue)
            .also { resistance = it }
    }

}