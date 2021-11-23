package com.anatolykravchenko.kipcalculator.rtd

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anatolykravchenko.kipcalculator.rtd.resistancetotemperautre.CopperSensorResistanceToTemp
import com.anatolykravchenko.kipcalculator.rtd.resistancetotemperautre.PSensorResistanceToTemp
import com.anatolykravchenko.kipcalculator.rtd.resistancetotemperautre.PTSensorResistanceToTemp
import com.anatolykravchenko.kipcalculator.rtd.temperatureToResistance.CopperTempToResistance
import com.anatolykravchenko.kipcalculator.rtd.temperatureToResistance.PSensorTempToResistance
import com.anatolykravchenko.kipcalculator.rtd.temperatureToResistance.PTSensorTempToResistance
import com.anatolykravchenko.kipcalculator.rtd.temperatureToResistance.TemperatureToResistanceSelector
import java.math.BigDecimal
import java.math.RoundingMode
import androidx.lifecycle.MutableLiveData as MutableLiveData


class RTDVM: ViewModel() {

    enum class SensorType {
        PlatinumPT, PlatinumP, Coopers
    }

    enum class OperationType {
        Temperature, Value
    }

    var nominalResistance: Double = 50.0
    var inputValue: Double = 0.0
    var materialType:SensorType = SensorType.Coopers
    var temperature: Double = 0.00
    var resistance: Double = 0.00
    var operationType: OperationType = OperationType.Temperature
    var result: Double = 0.00
    private val _resultString: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    var resultString: LiveData<String> = _resultString


    fun getTemperature(nominalResistance: Double, inputValue:Double):Double  {
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
        }
    }

    private fun temperatureToResistanceSelector(nominalResistance: Double,
                                                inputValue: Double): Double {
        return when (materialType) {
            SensorType.Coopers -> CopperTempToResistance(nominalResistance,
                inputValue).getOperationType(
                nominalResistance,
                inputValue
            )

            SensorType.PlatinumPT -> PTSensorTempToResistance(
                nominalResistance,
                inputValue
            ).getOperationType(
                nominalResistance,
                inputValue
            )

            SensorType.PlatinumP -> PSensorTempToResistance(nominalResistance,
                inputValue).getOperationType(
                nominalResistance,
                inputValue
            )
        }
    }

    fun getResult() {
        if(operationType == OperationType.Temperature)
        {
            val temp  = getTemperature(
                nominalResistance,
                inputValue)
         //   if(resultChecker(materialType, temp)==true) {

                _resultString.value = "Значение температуры ${temp.toBigDecimal().setScale(3,
                    RoundingMode.UP)}"
           // }
        }

        // if(operationType == RTDVM.OperationType.Value && inputChecker(materialType, inputValue))
        if(operationType == OperationType.Value)
        {
            val resistance = getResistance(
                nominalResistance,
                inputValue
            )
            _resultString.value = "Значение сопротивления ${resistance.toBigDecimal().setScale(3,
                RoundingMode.UP)}"

        }
    }
    }

//}