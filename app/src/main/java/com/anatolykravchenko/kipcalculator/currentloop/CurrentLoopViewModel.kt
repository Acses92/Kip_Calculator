package com.anatolykravchenko.kipcalculator.currentloop

import androidx.lifecycle.ViewModel

class CurrentLoopViewModel: ViewModel() {

    fun getCurrent(lowLimit: Double, highLimit: Double, value: Double): Double {
        return lowLimit
    }

    fun getValue(lowLimit: Double, highLimit: Double, current: Double): Double {

        return (((current-4.0)*(highLimit-lowLimit))/16)+lowLimit
    }
}