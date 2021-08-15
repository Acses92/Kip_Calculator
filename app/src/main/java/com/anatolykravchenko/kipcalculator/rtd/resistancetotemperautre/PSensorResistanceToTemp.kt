package com.anatolykravchenko.kipcalculator.rtd.resistancetotemperautre

import kotlin.math.pow

class PSensorResistanceToTemp(override var nominalResistance: Double,
                              override var resistance: Double): ResistanceToTemperatureInterface {

    override fun getOperationType(nominalResistance: Double, resistance: Double):Double {
        return if(resistance/nominalResistance<1) {
            PSensorResistanceToTemp(nominalResistance, resistance).getTemperatureFromResistanceMinus(nominalResistance,
                resistance)

        } else {
            PSensorResistanceToTemp(nominalResistance, resistance).getTemperatureFromResistancePlus(nominalResistance,
                resistance)
        }
    }
    override fun getTemperatureFromResistanceMinus(nominalResistance: Double,
                                                   resistance: Double): Double {
        val d1 = 251.903
        val d2 = 8.80035
        val d3 = -2.91506
        val d4 = 1.67611

        val inputNam: Double = resistance/nominalResistance - 1.00

        return  d1*inputNam+d2* Math.pow(inputNam, 2.0) + d3* inputNam.pow(3.0) +
                d4* Math.pow(inputNam, 4.0)
    }

    override fun getTemperatureFromResistancePlus(
        nominalResistance: Double,
        resistance: Double): Double {
        val aCoefficient: Double = 3.9690e-3
        val bCoefficient: Double = -5.841e-7

        return ((kotlin.math.sqrt(aCoefficient.pow(2.0) - 4 * bCoefficient * (1 - resistance / nominalResistance)))
                - aCoefficient) /(2 * bCoefficient)
    }
}
