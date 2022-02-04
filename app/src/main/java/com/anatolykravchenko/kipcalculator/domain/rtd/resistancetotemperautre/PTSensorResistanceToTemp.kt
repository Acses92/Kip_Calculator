package com.anatolykravchenko.kipcalculator.domain.rtd.resistancetotemperautre

import kotlin.math.pow

class PTSensorResistanceToTemp(override var nominalResistance: Double,
                               override var resistance: Double): ResistanceToTemperatureInterface {

    override fun getOperationType(nominalResistance: Double, resistance: Double):Double {
        return if(resistance/nominalResistance<1) {
            PTSensorResistanceToTemp(nominalResistance, resistance).getTemperatureFromResistanceMinus(nominalResistance,
                resistance)

        } else {
            PTSensorResistanceToTemp(nominalResistance, resistance).getTemperatureFromResistancePlus(nominalResistance,
                resistance)
        }
    }

    override fun getTemperatureFromResistanceMinus(nominalResistance: Double,
                                                   resistance: Double): Double {
        val d1 = 255.819
        val d2 = 9.14550
        val d3 = -2.92363
        val d4 = 1.79090

        val inputNam: Double = resistance/nominalResistance - 1.00

        return  d1*inputNam+d2* inputNam.pow(2.0) + d3* inputNam.pow(3.0) +
                d4* inputNam.pow(4.0)
    }
    override fun getTemperatureFromResistancePlus(
        nominalResistance: Double,
        resistance: Double): Double {
        val aCoefficient: Double = 3.9083e-3
        val bCoefficient: Double = -5.775e-7

        return ((kotlin.math.sqrt(aCoefficient.pow(2.0) - 4 * bCoefficient * (1 - resistance / nominalResistance)))
                - aCoefficient) /(2 * bCoefficient)
    }
}