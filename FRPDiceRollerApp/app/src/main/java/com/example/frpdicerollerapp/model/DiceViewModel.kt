package com.example.frpdicerollerapp.model

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class DiceViewModel : ViewModel() {
    var selectedDiceList by mutableStateOf(listOf<String>())
    var result by mutableStateOf("")
    var currentQuantity by mutableIntStateOf(1)

    fun rollDice() {
        val results = mutableListOf<String>()
        for (dice in selectedDiceList) {
            val sides = when (dice) {
                "D4" -> 4
                "D6" -> 6
                "D8" -> 8
                "D10" -> 10
                "D12" -> 12
                "D20" -> 20
                else -> 6
            }
            val rollResult = (1..sides).random()
            results.add("$dice: $rollResult")
        }
        result = results.joinToString(", ")
    }

    fun updateSelectedDice(dice: String, quantity: Int, isSelected: Boolean) {
        selectedDiceList = if (isSelected) {
            selectedDiceList + List(quantity) { dice }
        } else {
            selectedDiceList.filterNot { it == dice }
        }
    }
}
