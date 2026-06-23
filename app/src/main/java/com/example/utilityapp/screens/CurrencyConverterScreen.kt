package com.example.utilityapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.utilityapp.viewmodel.CurrencyViewModel

enum class CurrencyUnit(val label: String, val code: String) {
    USD("US Dollar", "USD"),
    EUR("Euro", "EUR"),
    GBP("British Pound", "GBP"),
    JPY("Japanese Yen", "JPY"),
    AUD("Australian Dollar", "AUD"),
    CAD("Canadian Dollar", "CAD"),
    INR("Indian Rupee", "INR")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen(viewModel: CurrencyViewModel = viewModel()) {
    var inputValue by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf(CurrencyUnit.USD) }
    var toUnit by remember { mutableStateOf(CurrencyUnit.EUR) }
    var fromExpanded by remember { mutableStateOf(value = false) }
    var toExpanded by remember { mutableStateOf(value = false) }
    var resultText by remember { mutableStateOf("") }

    val rates = viewModel.rates
    val isLoading = viewModel.isLoading
    val error = viewModel.error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Currency Converter", style = MaterialTheme.typography.headlineMedium)
        
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
            Button(onClick = { viewModel.fetchRates() }) {
                Text("Retry")
            }
        }

        Text(
            "Note: Using real-time exchange rates from API.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
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
            onExpandedChange = { fromExpanded = !fromExpanded },
        ) {
            OutlinedTextField(
                value = "${fromUnit.label} (${fromUnit.code})",
                onValueChange = {},
                readOnly = true,
                label = { Text("From") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fromExpanded) },
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
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
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
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
                val fromRate = rates[fromUnit.code] ?: 1.0
                val toRate = rates[toUnit.code] ?: 1.0
                
                // response is base on USD (or whatever base we used)
                // result = input * (toRate / fromRate)
                val result = inputAmount * (toRate / fromRate)
                resultText = "%.2f ${toUnit.code}".format(result)
            },
            enabled = rates.isNotEmpty() && !isLoading,
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
