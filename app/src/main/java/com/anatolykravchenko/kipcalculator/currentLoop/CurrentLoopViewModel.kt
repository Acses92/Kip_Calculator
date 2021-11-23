package com.anatolykravchenko.kipcalculator.currentLoop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import java.math.RoundingMode

enum class OperationType{
    Current, Value
}

class CurrentLoopViewModel: ViewModel() {

    var highLimit: Double = 0.0
    var lowLimit: Double = 0.0
    var value: Double = 0.0
    var currentOperationType: OperationType = OperationType.Current
    var status = MutableLiveData<Boolean>()


    private fun getCurrent(lowLimit: Double, highLimit: Double, value: Double): Double {
        return 16.0 * (value - lowLimit) / (highLimit - lowLimit) + 4.0
    }


    private fun getValue(lowLimit: Double, highLimit: Double, current: Double): Double {
        return (((current - 4.0) * (highLimit - lowLimit)) / 16) + lowLimit
    }

    private fun buttonValueIsClick(): BigDecimal {
        return getValue(lowLimit, highLimit, value).toBigDecimal().setScale(
            3, RoundingMode.UP
        )
    }

    private fun buttonCurrentIsClick(): BigDecimal {
        return getCurrent(lowLimit, highLimit, value).toBigDecimal().setScale(
            3,
            RoundingMode.UP
        )
    }

    fun buttonClicker(): BigDecimal {
        return if (currentOperationType == OperationType.Current) {
            buttonCurrentIsClick()
        } else {
            buttonValueIsClick()
        }
    }

    //Функция проверяет корректность введенных значений
    private fun inputValueChecker(): Boolean {
        if(lowLimit.isInfinite() || highLimit.isInfinite() || value.isInfinite()) {

            status.value = false
        }

        if(lowLimit>highLimit) {

            return false
        }

        if((value>highLimit || value<lowLimit) && currentOperationType ==OperationType.Value) {

            return false
        }

        if(currentOperationType==OperationType.Current && (value<4.0 || value>20.0)) {

            return false
        }

        if(highLimit.equals(0.00)) {

            return false
        }
        return true
    }
}


