package com.anatolykravchenko.kipcalculator

import kotlin.math.pow

interface ResistanceToTemperatureInterface {

    var  nominalResistance: Double
    var resistance: Double

    fun getOperationType(nominalResistance: Double, resistance: Double): Double {
        return 0.00
    }

    fun getTemperatureFromResistancePlus(nominalResistance: Double, resistance: Double):Double {
        //если не указать значение по-умолчанию чуда не произойдёт мазафака
        return 0.00
    }
    fun getTemperatureFromResistanceMinus(nominalResistance: Double, resistance: Double):Double {
        //аналогично
        return 0.00
    }
}

class ResistanceToTemperature(nominalResistance: Double, resistance: Double){

    fun sensorSelector(
        sensorTypeString: String,
        nominalResistance: Double,
        resistance: Double
    ): Double {

        return when (sensorTypeString) {
            //    SensorType.Nickel -> NickelSensor(nominalResistance, resistance)
            "Coopers" -> CopperSensor(nominalResistance, resistance).getOperationType(
                nominalResistance,
                resistance
            )
            "PlatinumPT" -> PlatinumSensorPT(nominalResistance, resistance).getOperationType(
                nominalResistance,
                resistance
            )
            "PlatinumP" -> PlatinumSensorP(nominalResistance, resistance).getOperationType(
                nominalResistance,
                resistance
            )
            else -> 0.00
        }
    }

    //Расчёт для датчика PT
     class PlatinumSensorPT(override var nominalResistance: Double,
                           override var resistance: Double): ResistanceToTemperatureInterface {

        override fun getOperationType(nominalResistance: Double, resistance: Double):Double {
            return if(resistance/nominalResistance<1) {
                PlatinumSensorPT(nominalResistance, resistance).getTemperatureFromResistanceMinus(nominalResistance,
                    resistance)

            } else {
                PlatinumSensorPT(nominalResistance, resistance).getTemperatureFromResistancePlus(nominalResistance,
                    resistance)
            }
        }

          override fun getTemperatureFromResistanceMinus(nominalResistance: Double,
                                                      resistance: Double): Double {
            super.getTemperatureFromResistancePlus(nominalResistance, resistance)
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
            super.getTemperatureFromResistanceMinus(nominalResistance, resistance)
            val aCoefficient: Double = 3.9083e-3
            val bCoefficient: Double = -5.775e-7

            return ((kotlin.math.sqrt(aCoefficient.pow(2.0) - 4 * bCoefficient * (1 - resistance / nominalResistance)))
                    - aCoefficient) /(2 * bCoefficient)
        }
    }

    //Расчёт для датчика П
    class PlatinumSensorP(override var nominalResistance: Double,
                           override var resistance: Double): ResistanceToTemperatureInterface {

        override fun getOperationType(nominalResistance: Double, resistance: Double):Double {
            return if(resistance/nominalResistance<1) {
                PlatinumSensorP(nominalResistance, resistance).getTemperatureFromResistanceMinus(nominalResistance,
                    resistance)

            } else {
                PlatinumSensorP(nominalResistance, resistance).getTemperatureFromResistancePlus(nominalResistance,
                    resistance)
            }
        }
        override fun getTemperatureFromResistanceMinus(nominalResistance: Double,
                                                       resistance: Double): Double {
            super.getTemperatureFromResistancePlus(nominalResistance, resistance)
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
            super.getTemperatureFromResistanceMinus(nominalResistance, resistance)
            val aCoefficient: Double = 3.9690e-3
            val bCoefficient: Double = -5.841e-7

            return ((kotlin.math.sqrt(aCoefficient.pow(2.0) - 4 * bCoefficient * (1 - resistance / nominalResistance)))
                    - aCoefficient) /(2 * bCoefficient)
        }
    }

    //Для медных датчиков
    class CopperSensor(override var nominalResistance: Double,
                       override var resistance: Double): ResistanceToTemperatureInterface {


        override fun getOperationType(nominalResistance: Double, resistance: Double):Double {
            return if(resistance/nominalResistance<1) {
                CopperSensor(nominalResistance, resistance).getTemperatureFromResistanceMinus(nominalResistance,
                    resistance)

            } else {
                CopperSensor(nominalResistance, resistance).getTemperatureFromResistancePlus(nominalResistance,
                    resistance)
            }
        }

        override fun getTemperatureFromResistanceMinus(
            nominalResistance: Double,
            resistance: Double
        ): Double {
            super.getTemperatureFromResistanceMinus(nominalResistance, resistance)

            val d1: Double = 233.87
            val d2: Double = 7.9370
            val d3: Double = -2.0062
            val d4: Double = -0.3953
            val inputNam: Double = resistance/nominalResistance - 1.00

            return d1*inputNam+d2* inputNam.pow(2.0) + d3* inputNam.pow(3.0) +
                    d4* Math.pow(inputNam, 4.0)

        }
        override fun getTemperatureFromResistancePlus(
            nominalResistance: Double,
            resistance: Double
        ): Double {
            super.getTemperatureFromResistancePlus(nominalResistance, resistance)
            val A: Double = 4.28e-3
            return (resistance/nominalResistance - 1)/A
        }
    }
/*
    class NickelSensor(override var nominalResistance: Double,
                       override var resistance: Double): ResistanceToTemperatureInterface {

    }
*/

}

