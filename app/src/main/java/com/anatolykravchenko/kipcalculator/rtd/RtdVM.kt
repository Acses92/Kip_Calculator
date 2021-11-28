package com.anatolykravchenko.kipcalculator.rtd

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.anatolykravchenko.kipcalculator.currentLoop.CurrentLoopErrorType
import com.anatolykravchenko.kipcalculator.currentLoop.SingleLiveEvent
import com.anatolykravchenko.kipcalculator.rtd.resistancetotemperautre.CopperSensorResistanceToTemp
import com.anatolykravchenko.kipcalculator.rtd.resistancetotemperautre.PSensorResistanceToTemp
import com.anatolykravchenko.kipcalculator.rtd.resistancetotemperautre.PTSensorResistanceToTemp
import com.anatolykravchenko.kipcalculator.rtd.temperatureToResistance.CopperTempToResistance
import com.anatolykravchenko.kipcalculator.rtd.temperatureToResistance.PSensorTempToResistance
import com.anatolykravchenko.kipcalculator.rtd.temperatureToResistance.PTSensorTempToResistance
import java.math.RoundingMode
import androidx.lifecycle.MutableLiveData as MutableLiveData

enum class RTDErrorType {
    WRONG_TEMPERATURE_LIMIT,
    WRONG_RESISTANCE_LIMIT
}

class RtdVM: ViewModel() {

    enum class SensorType {
        PlatinumPT, PlatinumP, Coopers
    }

    enum class OperationType {
        Temperature, Value
    }

    var nominalResistance: Double = 50.0
    var inputValue: Double = 0.0
    var materialType: SensorType = SensorType.Coopers
    var temperature: Double = 0.00
    var resistance: Double = 0.00
    var operationType: OperationType = OperationType.Temperature
    private var status: Boolean = true
    private val  _message = SingleLiveEvent<RTDErrorType>()
    val message: LiveData<RTDErrorType> = _message
    private val _resultString: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    var resultString: LiveData<String> = _resultString


    private fun getTemperature(nominalResistance: Double, inputValue: Double): Double {
        return resistanceToTemperatureSelector(nominalResistance, inputValue)
            .also { temperature = it }
    }

    private fun getResistance(nominalResistance: Double, inputValue: Double)
            : Double {
        return temperatureToResistanceSelector(nominalResistance, inputValue)
            .also { resistance = it }
    }

    private fun resistanceToTemperatureSelector(
        nominalResistance: Double,
        inputValue: Double
    ): Double {
        return when (materialType) {
            SensorType.Coopers -> CopperSensorResistanceToTemp(
                nominalResistance,
                inputValue
            ).getOperationType(
                nominalResistance,
                inputValue
            )
            SensorType.PlatinumPT -> PTSensorResistanceToTemp(
                nominalResistance,
                inputValue
            ).getOperationType(
                nominalResistance,
                inputValue
            )
            SensorType.PlatinumP -> PSensorResistanceToTemp(
                nominalResistance,
                inputValue
            ).getOperationType(
                nominalResistance,
                inputValue
            )
        }
    }

    private fun temperatureToResistanceSelector(
        nominalResistance: Double,
        inputValue: Double
    ): Double {
        return when (materialType) {
            SensorType.Coopers -> CopperTempToResistance(
                nominalResistance,
                inputValue
            ).getOperationType(
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

            SensorType.PlatinumP -> PSensorTempToResistance(
                nominalResistance,
                inputValue
            ).getOperationType(
                nominalResistance,
                inputValue
            )
        }
    }

    fun getResult() {
        inputChecker(materialType, inputValue)
        if (operationType == OperationType.Temperature) {
            val temp = getTemperature(nominalResistance, inputValue)
            resultChecker(materialType, temp)
            if (status) {
                _resultString.value = "Значение температуры ${
                    temp.toBigDecimal().setScale(
                        3,
                        RoundingMode.UP)}"

            }
        }
        // if(operationType == RTDVM.OperationType.Value && inputChecker(materialType, inputValue))
        if (operationType == OperationType.Value && status) {
            val resistance = getResistance(
                nominalResistance,
                inputValue
            )
            _resultString.value = "Значение сопротивления ${resistance.toBigDecimal().setScale(
                    3,
                    RoundingMode.UP)}"
        }
        status = true
    }

    private fun resultChecker(materialType: RtdVM.SensorType, result: Double) {
        if(result>850.00 && (materialType==SensorType.PlatinumP
                    || materialType==SensorType.PlatinumPT)) {
            _message.value = RTDErrorType.WRONG_RESISTANCE_LIMIT
            status = false
        }

        if((result<-180.0 || result>200) && materialType == RtdVM.SensorType.Coopers )
        {
            _message.value = RTDErrorType.WRONG_RESISTANCE_LIMIT
            status = false
        }
    }


    private fun inputChecker(materialType: RtdVM.SensorType, inputTemperature: Double) {
        if( (materialType ==RtdVM.SensorType.Coopers) && inputTemperature>200.0) {
            _message.value = RTDErrorType.WRONG_TEMPERATURE_LIMIT
            status = false
        }
        if((materialType ==RtdVM.SensorType.PlatinumP ||
                    materialType==RtdVM.SensorType.PlatinumPT) && inputTemperature>850.0) {
            _message.value = RTDErrorType.WRONG_TEMPERATURE_LIMIT
            status = false

        }
    }
}

//}