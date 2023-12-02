package com.example.frpdicerollerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


class MainActivity : ComponentActivity() {

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceRoller()
        }
    }

    @Composable
    fun DiceRoller(diceViewModel: DiceViewModel = viewModel()) {
        val diceOptions = resources.getStringArray(R.array.dropdown_items).toList()
        val rollText = resources.getString(R.string.roll_text)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Select Dice:" ,style = MaterialTheme.typography.headlineSmall)
            DisplayQuantityButtons(onQuantitySelected = { i -> diceViewModel.currentQuantity = i})
            Spacer(Modifier.height(16.dp))

            DropdownMenuDies(
                options = diceOptions,
                selectedOptions = diceViewModel.selectedDiceList,
                onDiceSelected = { dice,isSelected ->
                    diceViewModel.updateSelectedDice(dice, diceViewModel.currentQuantity, isSelected)
                }
            )
            Text("Quantity Amount: ${diceViewModel.currentQuantity}",style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(100.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                DisplayDieResultText(diceViewModel)
            }
            Spacer(Modifier.height(100.dp))
            Text("Selected Die(s): ${
                diceViewModel.selectedDiceList.groupingBy { it }.eachCount()
                    .entries.joinToString { "${it.key} x${it.value}" }
            }", style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(5.dp))
            Button(onClick = { diceViewModel.rollDice() },
                modifier = Modifier.width(100.dp)
                .height(50.dp)) {
                Text(rollText)
            }

            Spacer(Modifier.height(16.dp))

        }
    }

    @Composable
    fun DropdownMenuDies(
        options: List<String>,
        selectedOptions: List<String>,
        onDiceSelected: (String, Boolean) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        Column {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier
                    .width(300.dp)
                    .height(60.dp)
            ) {
                Text(selectedOptions.toString())
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { label ->
                    val isSelected = label in selectedOptions
                    DropdownMenuItem(
                        text = {Text(text = label)},
                        onClick = {
                            onDiceSelected(label, !isSelected)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                        ,
                    )
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Green
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun DisplayQuantityButtons(
        onQuantitySelected: (Int) -> Unit
    ) {
        Row {
            (1..3).forEach { quantity ->
                Button(
                    onClick = { onQuantitySelected(quantity)
                              },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        quantity.toString(),
                        color = Color.White
                    )
                }
            }
        }
    }


    @Composable
    fun DisplayDieResultText(diceViewModel : DiceViewModel){
        if (diceViewModel.result.isNotEmpty()) {
            val resultString = buildString {
                append("Result: ")
                diceViewModel.result.split(", ").forEachIndexed { index, result ->
                    if (index > 0) append(", ")
                    append(result+"\t")
                }
            }
            Text(resultString, style = MaterialTheme.typography.titleLarge)
        }
    }



    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        DiceRoller()
    }

}
