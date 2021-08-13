package com.anatolykravchenko.kipcalculator

import java.lang.StrictMath.pow
import kotlin.math.pow

interface TemperatureToResistanceInterface {
    var nominalResistance :Double
    var temperature: Double
    val aCoef: Double
    val bCoef: Double
    val cCoef: Double

    fun getOperationType(nominalResistance: Double, temperature: Double ): Double {return 0.00 }
    fun getResistanceFromTemperaturePlus(nominalResistance: Double, temperature: Double): Double {
        return 0.00
    }
    fun getResistanceFromTemperatureMinus(nominalResistance: Double, temperature: Double): Double {
        return 0.00
    }
}

class TemperatureToResistance(nominalResistance: Double, temperature: Double) {

    fun sensorSelector(
        sensorTypeString: String,
        nominalResistance: Double,
        temperature: Double
    ): Double {
        return when (sensorTypeString) {
            //    SensorType.Nickel -> NickelSensor(nominalResistance, resistance)

            else -> 0.00
        }
    }

    class PTSensorTempToResistance(override var nominalResistance: Double,
                                   override var temperature: Double
    ): TemperatureToResistanceInterface
    {
        override val aCoef: Double = 3.9083e-3
        override val bCoef: Double = -5.775e-7
        override val cCoef: Double = -4.183e-12

        override fun getOperationType(nominalResistance: Double, temperature: Double): Double {
            return if(temperature > 0) {getResistanceFromTemperatureMinus(nominalResistance,
                temperature)}
            else {
                getResistanceFromTemperaturePlus(nominalResistance, temperature) }
            }
        override fun getResistanceFromTemperatureMinus(
            nominalResistance: Double,
            temperature: Double
        ): Double {
            return nominalResistance*(1+aCoef*temperature+bCoef* temperature.pow(2.00) +
                    cCoef*(temperature-100.00)* temperature.pow(3.00))
        }

        override fun getResistanceFromTemperaturePlus(
            nominalResistance: Double,
            temperature: Double
        ): Double {
            return nominalResistance*(1+aCoef*temperature+bCoef*temperature.pow(2.00))
        }
    }

    class PSensorTempToResistance(override var nominalResistance: Double,
                                  override var temperature: Double
    ): TemperatureToResistanceInterface {
        override val aCoef: Double = 3.9690e-3
        override val bCoef: Double = -5.841e-7
        override val cCoef: Double = -4.330e-12

        override fun getOperationType(nominalResistance: Double, temperature: Double): Double {
            return if(temperature > 0) {getResistanceFromTemperatureMinus(nominalResistance,
                temperature)}
            else {
                getResistanceFromTemperaturePlus(nominalResistance, temperature) }
        }

        override fun getResistanceFromTemperatureMinus(
            nominalResistance: Double,
            temperature: Double
        ): Double {
            return nominalResistance*(1+aCoef*temperature+bCoef* temperature.pow(2.00) +
                    cCoef*(temperature-100.00)* temperature.pow(3.00))
        }

        override fun getResistanceFromTemperaturePlus(
            nominalResistance: Double,
            temperature: Double
        ): Double {
            return nominalResistance*(1+aCoef*temperature+bCoef*temperature.pow(2.00))
        }
    }

    class CopperTempToResistance(
        override var nominalResistance: Double,
        override var temperature: Double
    ): TemperatureToResistanceInterface {
        override val aCoef: Double = 3.9690e-3
        override val bCoef: Double = -5.841e-7
        override val cCoef: Double = -4.330e-12

        override fun getOperationType(nominalResistance: Double, temperature: Double):Double {
            return if(temperature > 0) {getResistanceFromTemperatureMinus(nominalResistance,
                temperature)}
            else {
                getResistanceFromTemperaturePlus(nominalResistance, temperature) }
        }

        override fun getResistanceFromTemperatureMinus(
            nominalResistance: Double,
            temperature: Double
        ): Double {
            return nominalResistance*(1+aCoef*temperature+bCoef* temperature.pow(2.00) +
                    cCoef*(temperature+6.7)* temperature.pow(3.00))
        }

        override fun getResistanceFromTemperaturePlus(
            nominalResistance: Double,
            temperature: Double
        ): Double {
            return nominalResistance*(1.00+aCoef*temperature+bCoef*temperature.pow(2.00))
        }
    }

}
