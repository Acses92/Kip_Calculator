package com.anatolykravchenko.kipcalculator

interface ITemperatureToResistanceInterface {
    var nominalResistance: Double
    var temperature: Double
    val aCoef: Double
    val bCoef: Double
    val cCoef: Double
    fun getOperationType(nominalResistance: Double, temperature: Double): Double
    fun getResistanceFromTemperaturePlus(nominalResistance: Double, temperature: Double): Double
    fun getResistanceFromTemperatureMinus(nominalResistance: Double, temperature: Double): Double
}