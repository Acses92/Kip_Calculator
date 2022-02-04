package com.anatolykravchenko.kipcalculator.domain.rtd.temperatureToResistance


class TemperatureToResistanceSelector {

    fun sensorSelector(
        sensorTypeString: String,
        nominalResistance: Double,
        temperature: Double
    ): Double {
        return when (sensorTypeString) {
            //    SensorType.Nickel -> NickelSensor(nominalResistance, resistance)
            "Coopers" -> CopperTempToResistance(nominalResistance, temperature).getOperationType(
                nominalResistance,
                temperature
            )
            "PlatinumPT" -> PTSensorTempToResistance(
                nominalResistance,
                temperature
            ).getOperationType(
                nominalResistance,
                temperature
            )
            "PlatinumP" -> PSensorTempToResistance(nominalResistance, temperature).getOperationType(
                nominalResistance,
                temperature
            )
            else -> 0.00
        }
    }
}