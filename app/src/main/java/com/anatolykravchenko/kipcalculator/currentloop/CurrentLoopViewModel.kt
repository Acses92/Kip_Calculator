package com.anatolykravchenko.kipcalculator.currentloop

import android.widget.Toast
import androidx.lifecycle.ViewModel

class CurrentLoopViewModel: ViewModel() {

    var highLimit: Double = 0.0
    var lowLimit: Double = 0.0
    var value: Double = 0.0

    fun getCurrent(lowLimit: Double, highLimit: Double, value: Double): Double {
        return lowLimit
    }

    fun getValue(lowLimit: Double, highLimit: Double, current: Double): Double {

        return (((current - 4.0) * (highLimit - lowLimit)) / 16) + lowLimit
    }

}