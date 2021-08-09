package com.anatolykravchenko.kipcalculator

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

class ResistanceToTemperature {

    //Расчёт для датчика PT
     class PlatinumSensorPT(override var nominalResistance: Double,
                           override var resistance: Double): ResistanceToTemperatureInterface {

        override fun getOperationType(nominalResistance: Double, resistance: Double):Double {
            return if(resistance/nominalResistance<1) {
                PlatinumSensorPT(nominalResistance, resistance).getTemperatureFromResistanceMinus(nominalResistance,
                    resistance)

            } else {
                PlatinumSensorPT(nominalResistance, resistance).getTemperatureFromResistanceMinus(nominalResistance,
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

            return  String.format("%.3f",d1*inputNam+d2* Math.pow(inputNam, 2.0) + d3* Math.pow(inputNam, 3.0) +
                     d4* Math.pow(inputNam, 4.0)).toDouble()
        }
        override fun getTemperatureFromResistancePlus(
            nominalResistance: Double,
            resistance: Double): Double {
            super.getTemperatureFromResistanceMinus(nominalResistance, resistance)
            val aCoefficient: Double = 3.9083e-3
            val bCoefficient: Double = -5.775e-7

            return String.format("%.3f",((kotlin.math.sqrt(Math.pow(aCoefficient, 2.0) - 4 * bCoefficient * (1 - resistance / nominalResistance)))
                    - aCoefficient) /(2 * bCoefficient)).toDouble()
        }
    }

    //Расчёт для датчика П
    class PlatinumSensorP(override var nominalResistance: Double,
                           override var resistance: Double): ResistanceToTemperatureInterface {


        override fun getTemperatureFromResistanceMinus(nominalResistance: Double,
                                                       resistance: Double): Double {
            super.getTemperatureFromResistancePlus(nominalResistance, resistance)
            val d1 = 251.903
            val d2 = 8.80035
            val d3 = -2.91506
            val d4 = 1.67611

            val inputNam: Double = resistance/nominalResistance - 1.00

            return  d1*inputNam+d2* Math.pow(inputNam, 2.0) + d3* Math.pow(inputNam, 3.0) +
                    d4* Math.pow(inputNam, 4.0)
        }

        override fun getTemperatureFromResistancePlus(
            nominalResistance: Double,
            resistance: Double): Double {
            super.getTemperatureFromResistanceMinus(nominalResistance, resistance)
            val aCoefficient: Double = 3.9690-3
            val bCoefficient: Double = -5.841e-7

            return String.format("%.3f",(((kotlin.math.sqrt(Math.pow(aCoefficient, 2.0) - 4 * bCoefficient * (1 - resistance / nominalResistance)))
                    - aCoefficient) /(2 * bCoefficient))).toDouble()
        }
    }

    //Для медных датчиков
    class CopperSensor(override var nominalResistance: Double,
                       override var resistance: Double): ResistanceToTemperatureInterface {

    }


}

