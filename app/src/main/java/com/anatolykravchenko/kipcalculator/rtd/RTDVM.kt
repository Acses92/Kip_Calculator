package com.anatolykravchenko.kipcalculator.rtd

import androidx.lifecycle.ViewModel
import com.anatolykravchenko.kipcalculator.SensorType


class RTDVM: ViewModel() {

    var nominalResistance: Double = 50.0
    var inputValue: Double = 0.0
    var materialTypeString: String = ""
    lateinit var materialType: Enum<SensorType>
}