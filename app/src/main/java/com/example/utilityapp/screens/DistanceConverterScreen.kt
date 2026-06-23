package com.example.utilityapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

enum class DistanceUnit(val label: String, val factorToMeters: Double) {
    METERS("Meters (m)", 1.0),
    KILOMETERS("Kilometers (km)", 1000.0),
    MILES("Miles (mi)", 1609.34),
    FEET("Feet (ft)", 0.3048),
    INCHES("Inches (in)", 0.0254)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistanceConverterScreen() {
    var inputValue by rememberSaveable { mutableStateOf("") }
    var fromUnit by rememberSaveable { mutableStateOf(DistanceUnit.METERS) }
    var toUnit by rememberSaveable { mutableStateOf(DistanceUnit.KILOMETERS) }
    var fromExpanded by rememberSaveable { mutableStateOf(value = false) }
    var toExpanded by rememberSaveable { mutableStateOf(value = false) }
    var resultText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Distance Converter", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = inputValue,
            onValueChange = { 
                inputValue = it 
                resultText = "" // Clear result when input changes
            },
            label = { Text("Enter value") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
        )

        // From Unit Selection
        ExposedDropdownMenuBox(
            expanded = fromExpanded,
            onExpandedChange = { fromExpanded = !fromExpanded },
        ) {
            OutlinedTextField(
                value = fromUnit.label,
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
                DistanceUnit.entries.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit.label) },
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
                value = toUnit.label,
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
                DistanceUnit.entries.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit.label) },
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
                val result = (inputAmount * fromUnit.factorToMeters) / toUnit.factorToMeters
                resultText = "%.2f ${toUnit.label.split(" ").last()}".format(result)
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
