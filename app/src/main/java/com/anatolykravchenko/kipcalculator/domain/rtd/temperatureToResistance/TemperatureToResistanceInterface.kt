package com.anatolykravchenko.kipcalculator.domain.rtd.temperatureToResistance

interface TemperatureToResistanceInterface {
    var nominalResistance: Double
    var temperature: Double
    val aCoef: Double
    val bCoef: Double
    val cCoef: Double
    fun getOperationType(nominalResistance: Double, temperature: Double): Double
    fun getResistanceFromTemperaturePlus(nominalResistance: Double, temperature: Double): Double
    fun getResistanceFromTemperatureMinus(nominalResistance: Double, temperature: Double): Double
}