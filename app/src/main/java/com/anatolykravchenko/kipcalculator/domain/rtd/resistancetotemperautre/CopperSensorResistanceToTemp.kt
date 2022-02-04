package com.anatolykravchenko.kipcalculator.domain.rtd.resistancetotemperautre

import kotlin.math.pow

class CopperSensorResistanceToTemp(override var nominalResistance: Double,
                                   override var resistance: Double): ResistanceToTemperatureInterface {


    override fun getOperationType(nominalResistance: Double, resistance: Double):Double {
        return if(resistance/nominalResistance<1) {
            CopperSensorResistanceToTemp(nominalResistance, resistance).getTemperatureFromResistanceMinus(nominalResistance,
                resistance)

        } else {
            CopperSensorResistanceToTemp(nominalResistance, resistance).getTemperatureFromResistancePlus(nominalResistance,
                resistance)
        }
    }

    override fun getTemperatureFromResistanceMinus(
        nominalResistance: Double,
        resistance: Double
    ): Double {

        val d1: Double = 233.87
        val d2: Double = 7.9370
        val d3: Double = -2.0062
        val d4: Double = -0.3953
        val inputNam: Double = resistance/nominalResistance - 1.00

        return d1*inputNam+d2* inputNam.pow(2.0) + d3* inputNam.pow(3.0) +
                d4* inputNam.pow(4.0)

    }
    override fun getTemperatureFromResistancePlus(
        nominalResistance: Double,
        resistance: Double
    ): Double {
        val A: Double = 4.28e-3
        return (resistance/nominalResistance - 1)/A
    }
}