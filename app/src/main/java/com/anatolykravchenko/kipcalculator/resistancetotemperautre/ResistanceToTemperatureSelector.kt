package com.anatolykravchenko.kipcalculator.resistancetotemperautre

class ResistanceToTemperatureSelector{

    fun sensorSelector(
        sensorTypeString: String,
        nominalResistance: Double,
        resistance: Double
    ): Double {

        return when (sensorTypeString) {
            //    SensorType.Nickel -> NickelSensor(nominalResistance, resistance)
            "Coopers" -> CopperSensorResistanceToTemp(nominalResistance, resistance).getOperationType(
                nominalResistance,
                resistance
            )
            "PlatinumPT" -> PTSensorResistanceToTemp(nominalResistance, resistance).getOperationType(
                nominalResistance,
                resistance
            )
            "PlatinumP" -> PSensorResistanceToTemp(nominalResistance, resistance).getOperationType(
                nominalResistance,
                resistance
            )
            else -> 0.00
        }
    }
}