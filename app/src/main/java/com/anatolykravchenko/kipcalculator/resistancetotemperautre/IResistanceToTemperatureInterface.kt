package com.anatolykravchenko.kipcalculator.resistancetotemperautre

interface IResistanceToTemperatureInterface {
    var  nominalResistance: Double
    var resistance: Double
    fun getOperationType(nominalResistance: Double, resistance: Double): Double
    fun getTemperatureFromResistancePlus(nominalResistance: Double, resistance: Double): Double
    fun getTemperatureFromResistanceMinus(nominalResistance: Double, resistance: Double): Double
}