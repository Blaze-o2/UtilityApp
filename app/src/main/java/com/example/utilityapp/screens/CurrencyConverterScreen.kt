package com.example.utilityapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

enum class CurrencyUnit(val label: String, val code: String, val rateToUsd: Double) {
    USD("US Dollar", "USD", 1.0),
    EUR("Euro", "EUR", 1.08),
    GBP("British Pound", "GBP", 1.27),
    JPY("Japanese Yen", "JPY", 0.0067),
    AUD("Australian Dollar", "AUD", 0.66),
    CAD("Canadian Dollar", "CAD", 0.74),
    INR("Indian Rupee", "INR", 0.012)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen() {
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf(CurrencyUnit.USD) }
    var toUnit by remember { mutableStateOf(CurrencyUnit.EUR) }
    var fromExpanded by remember { mutableStateOf(value = false) }
    var toExpanded by remember { mutableStateOf(value = false) }
    var resultText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Currency Converter", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Note: Using fixed exchange rates for demonstration.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )

        OutlinedTextField(
            value = inputValue,
            onValueChange = { 
                inputValue = it 
                resultText = ""
            },
            label = { Text("Enter amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
        )

        // From Unit Selection
        ExposedDropdownMenuBox(
            expanded = fromExpanded,
            onExpandedChange = { fromExpanded = !fromExpanded }
        ) {
            OutlinedTextField(
                value = "${fromUnit.label} (${fromUnit.code})",
                onValueChange = {},
                readOnly = true,
                label = { Text("From") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fromExpanded) },
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = fromExpanded,
                onDismissRequest = { fromExpanded = false }
            ) {
                CurrencyUnit.entries.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text("${unit.label} (${unit.code})") },
                        onClick = {
                            fromUnit = unit
                            fromExpanded = false
                            resultText = ""
                        }
                    )
                }
            }
        }

        // To Unit Selection
        ExposedDropdownMenuBox(
            expanded = toExpanded,
            onExpandedChange = { toExpanded = !toExpanded }
        ) {
            OutlinedTextField(
                value = "${toUnit.label} (${toUnit.code})",
                onValueChange = {},
                readOnly = true,
                label = { Text("To") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = toExpanded) },
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = toExpanded,
                onDismissRequest = { toExpanded = false }
            ) {
                CurrencyUnit.entries.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text("${unit.label} (${unit.code})") },
                        onClick = {
                            toUnit = unit
                            toExpanded = false
                            resultText = ""
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                val inputAmount = inputValue.toDoubleOrNull() ?: 0.0
                // Convert fromUnit to USD, then USD to toUnit
                val result = (inputAmount * fromUnit.rateToUsd) / toUnit.rateToUsd
                resultText = "%.2f ${toUnit.code}".format(result)
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Convert")
        }

        if (resultText.isNotEmpty()) {
            Text(
                text = "Result: $resultText",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
