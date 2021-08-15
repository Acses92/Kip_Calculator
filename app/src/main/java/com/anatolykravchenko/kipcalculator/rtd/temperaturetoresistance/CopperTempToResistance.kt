package com.anatolykravchenko.kipcalculator.rtd.temperaturetoresistance

import kotlin.math.pow

class CopperTempToResistance(
    override var nominalResistance: Double,
    override var temperature: Double
): TemperatureToResistanceInterface {
    override val aCoef: Double = 4.28e-3
    override val bCoef: Double = -6.2032e-7
    override val cCoef: Double = 8.5154e-12

    override fun getOperationType(nominalResistance: Double, temperature: Double):Double {
        return if(temperature > 0) {getResistanceFromTemperaturePlus(nominalResistance,
            temperature)}
        else {
            getResistanceFromTemperatureMinus(nominalResistance, temperature) }
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
        return nominalResistance*(1.00+aCoef*temperature)
    }
}
