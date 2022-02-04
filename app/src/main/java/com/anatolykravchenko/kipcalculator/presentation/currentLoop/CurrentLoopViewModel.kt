package com.anatolykravchenko.kipcalculator.presentation.currentLoop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anatolykravchenko.kipcalculator.presentation.SingleLiveEvent
import java.math.RoundingMode




class CurrentLoopViewModel: ViewModel() {

    var highLimit: Double = 0.0
    var lowLimit: Double = 0.0
    var value: Double = 0.0
    var currentOperationType: OperationType = OperationType.Current
    var status : Boolean = true
    private val  _message = SingleLiveEvent<CurrentLoopErrorType>()
    val message: LiveData<CurrentLoopErrorType> = _message
    private val _result: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val result: LiveData<String> = _result

    private fun getCurrent(lowLimit: Double, highLimit: Double, value: Double): Double {
        return 16.0 * (value - lowLimit) / (highLimit - lowLimit) + 4.0
    }

    private fun getValue(lowLimit: Double, highLimit: Double, current: Double): Double {
        return (((current - 4.0) * (highLimit - lowLimit)) / 16) + lowLimit
    }

    private fun buttonValueIsClick() {
        if(status == true) {
        val out = getValue(lowLimit, highLimit, value)
        _result.value = "Значение измеряемой величины ${
            out.toBigDecimal().setScale(
                3,
            RoundingMode.UP)}"
        }
    }

    private fun buttonCurrentIsClick() {
        if(status == true) {
            val out = getCurrent(lowLimit, highLimit, value)
            _result.value = "Значение тока ${
                out.toBigDecimal().setScale(
                    3,
                    RoundingMode.UP
                )
            }"
        }
    }

    fun buttonClicker() {
        if (currentOperationType == OperationType.Current) {
            inputValueChecker()
            buttonCurrentIsClick()
            status = true
        } else {
            inputValueChecker()
            buttonValueIsClick()
            status = true
        }
    }

    //Функция проверяет корректность введенных значений
    private fun inputValueChecker() {
        if(lowLimit.isInfinite() || highLimit.isInfinite() || value.isInfinite()) {

            _message.value = CurrentLoopErrorType.WRONG_CURRENT_LIMITS
            status = false
        }
        if(lowLimit>highLimit) {
            _message.value = CurrentLoopErrorType.LOW_LIMIT_MORE_HIGH_LIMIT
           status = false
        }
        if((value>highLimit || value<lowLimit) && currentOperationType ==OperationType.Value) {
            _message.value = CurrentLoopErrorType.WRONG_VALUE_LIMITS
            status = false
        }
        if(currentOperationType==OperationType.Value && (value<4.0 || value>20.0)) {
            _message.value = CurrentLoopErrorType.WRONG_CURRENT_LIMITS
            status = false
        }
        if(highLimit.equals(0.00)) {

            _message.value = CurrentLoopErrorType.WRONG_HIGH_LIMIT
            status = false
        }
    }
}



