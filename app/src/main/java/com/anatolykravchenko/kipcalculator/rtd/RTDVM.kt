package com.anatolykravchenko.kipcalculator.rtd

import androidx.lifecycle.ViewModel
import com.anatolykravchenko.kipcalculator.rtd.resistancetotemperautre.CopperSensorResistanceToTemp
import com.anatolykravchenko.kipcalculator.rtd.resistancetotemperautre.PSensorResistanceToTemp
import com.anatolykravchenko.kipcalculator.rtd.resistancetotemperautre.PTSensorResistanceToTemp
import com.anatolykravchenko.kipcalculator.rtd.temperatureToResistance.TemperatureToResistanceSelector


class RTDVM: ViewModel() {

    enum class SensorType {
        PlatinumPT, PlatinumP, Coopers
    }

    var nominalResistance: Double = 50.0
    var inputValue: Double = 0.0
    var materialTypeString: String = ""
    lateinit var materialType: Enum<SensorType>
    var temperature: Double = 0.00
    var resistance: Double = 0.00

    fun getTemperature(nominalResistance: Double, inputValue:Double)
    :Double  {
       return resistanceToTemperatureSelector(nominalResistance, inputValue)
           .also { temperature = it }
    }

    fun getResistance(nominalResistance: Double, inputValue:Double)
    :Double  {
        return temperatureToResistanceSelector(nominalResistance, inputValue)
            .also { resistance = it }
    }

    private fun resistanceToTemperatureSelector(nominalResistance: Double,
                                                inputValue:Double): Double {
        return when(materialType) {
            SensorType.Coopers -> CopperSensorResistanceToTemp(nominalResistance,
                inputValue).getOperationType(
                nominalResistance,
                inputValue
            )
            SensorType.PlatinumPT -> PTSensorResistanceToTemp(nominalResistance,
                inputValue).getOperationType(
                nominalResistance,
                inputValue
            )
            SensorType.PlatinumP -> PSensorResistanceToTemp(nominalResistance,
                inputValue).getOperationType(
                nominalResistance,
                inputValue
            )
            else -> 0.00
        }

    }

    private fun temperatureToResistanceSelector(nominalResistance: Double,
                                                inputValue: Double): Double {
        return when (materialType) {
            SensorType.Coopers -> CopperSensorResistanceToTemp(
                nominalResistance,
                inputValue
            ).getOperationType(nominalResistance, inputValue)

            SensorType.PlatinumPT -> PTSensorResistanceToTemp(
                nominalResistance,
                inputValue
            ).getOperationType(
                nominalResistance, inputValue
            )

            SensorType.PlatinumP -> PSensorResistanceToTemp(
                nominalResistance,
                inputValue
            ).getOperationType(
                nominalResistance,
                inputValue
            )
            else -> 0.00
        }
    }

}