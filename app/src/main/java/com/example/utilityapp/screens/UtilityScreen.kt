package com.example.utilityapp.screens

import android.widget.Button

@Composable
fun UtilityScreen() {
    var counter by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        org.w3c.dom.Text("Utility Screen", style = MaterialTheme.typography.headlineMedium)
        org.w3c.dom.Text("Counter: $counter", style = MaterialTheme.typography.bodyLarge)

        Button(onClick = { counter++ }) {
            org.w3c.dom.Text("Increment")
        }
    }
}