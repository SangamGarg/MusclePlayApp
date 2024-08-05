package com.sangam.muscleplay.appUtils

import com.shawnlin.numberpicker.NumberPicker

object AppConvertUnitsUtil {
    fun convertUnitHeight(
        selectedValueHeight: String,
        numberPickerArrayHeight: Array<String>,
        numberPickerHeight: NumberPicker
    ): String {
        var value: String = ""
        when (selectedValueHeight) {
            "centimeters (cm)" -> {
                value = numberPickerArrayHeight[numberPickerHeight.value]

            }

            "meters (m)" -> {
                value =
                    ((numberPickerArrayHeight[numberPickerHeight.value].toFloat() * 100).toInt()).toString()

            }

            "feet (ft)" -> {

                val number = numberPickerArrayHeight[numberPickerHeight.value].split(",")
                val integerPart = number[0].toInt()
                val fractionalPart = number.getOrElse(1) { "0" }.toInt()
                value = String.format("%.2f", ((integerPart * 30.48) + (fractionalPart * 2.54)))

            }


            "inch (in)" -> {
                val inches = numberPickerArrayHeight[numberPickerHeight.value].toFloat()
                value = String.format("%.2f", (inches * 2.54))
            }


        }
        return value
    }

    fun convertUnitWeight(
        selectedValueWeight: String,
        numberPickerArrayWeight: Array<String>,
        numberPickerWeight: NumberPicker
    ): String {
        var value: String = ""
        when (selectedValueWeight) {
            "kilograms (kg)" -> {
                value = numberPickerArrayWeight[numberPickerWeight.value]

            }

            "pound (lbs)" -> {
                value =
                    ((numberPickerArrayWeight[numberPickerWeight.value].toInt() / 2.2).toInt()).toString()

            }

        }
        return value
    }
}