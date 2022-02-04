package com.anatolykravchenko.kipcalculator.domain.rtd.resistancetotemperautre

interface ResistanceToTemperatureInterface {
    var  nominalResistance: Double
    var resistance: Double
    fun getOperationType(nominalResistance: Double, resistance: Double): Double
    fun getTemperatureFromResistancePlus(nominalResistance: Double, resistance: Double): Double
    fun getTemperatureFromResistanceMinus(nominalResistance: Double, resistance: Double): Double
}