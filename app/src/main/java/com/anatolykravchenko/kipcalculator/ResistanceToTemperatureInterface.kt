package com.anatolykravchenko.kipcalculator

interface ResistanceToTemperatureInterface {

    var  nominalResistance: Double
    var resistance: Double

    fun getTemperatureFromResistancePlus(nominalResistance: Double, resistance: Double):Double {
        //если не указать значение по-умолчанию чуда не произойдёт мазафака
        return 0.00
    }
    fun getTemperatureFromResistanceMinus(nominalResistance: Double, resistance: Double):Double {
        //аналогично
        return 0.00
    }
}

class ResistanceToTemperature {

    class PlatinaSensor(override var nominalResistance: Double,
    override var resistance: Double): ResistanceToTemperatureInterface {


        fun getOperationType(nominalResistance: Double, resistance: Double):Double {
            return if(resistance/nominalResistance<1) {
                getTemperatureFromResistanceMinus(nominalResistance, resistance)
            } else {
                getTemperatureFromResistancePlus(nominalResistance, resistance)
            }
        }

        override fun getTemperatureFromResistancePlus(nominalResistance: Double,
                                                      resistance: Double): Double {
            super.getTemperatureFromResistancePlus(nominalResistance, resistance)
            val d1 = 255.819
            val d2 = 9.14550
            val d3 = -2.92363
            val d4 = 1.79090

            val inputNam: Double = resistance/nominalResistance - 1.00

            return  d1*inputNam+d2* Math.pow(inputNam, 2.0) + d3* Math.pow(inputNam, 3.0) +
                     d4* Math.pow(inputNam, 4.0)
        }

        override fun getTemperatureFromResistanceMinus(
            nominalResistance: Double,
            resistance: Double): Double {
            super.getTemperatureFromResistanceMinus(nominalResistance, resistance)
            val aCoefficient: Double = 3.9083e-3
            val bCoefficient: Double = -5.775e-7

            return (((kotlin.math.sqrt(Math.pow(aCoefficient, 2.0) - 4 * bCoefficient * (1 - resistance / nominalResistance)))
                    - aCoefficient) /(2 * bCoefficient))
        }
    }
}

