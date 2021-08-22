package com.anatolykravchenko.kipcalculator.currentloop

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Exception

class CurrentLoopViewModel: ViewModel() {

    var highLimit: Double = 0.0
    var lowLimit: Double = 0.0
    var value: Double = 0.0
    var currentOperationType: Boolean = false


    fun getCurrent(lowLimit: Double, highLimit: Double, value: Double): Double {
        return 16.0*(value-lowLimit)/(highLimit-lowLimit)+4.0
    }

    fun getValue(lowLimit: Double, highLimit: Double, current: Double): Double {
            return (((current - 4.0) * (highLimit - lowLimit)) / 16) + lowLimit
    }
}