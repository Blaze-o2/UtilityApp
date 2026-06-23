package com.example.utilityapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.utilityapp.network.RetrofitClient
import kotlinx.coroutines.launch

class CurrencyViewModel : ViewModel() {
    var rates by mutableStateOf<Map<String, Double>>(emptyMap())
        private set
    
    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    init {
        fetchRates()
    }

    fun fetchRates(base: String = "USD") {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val response = RetrofitClient.instance.getLatestRates(base)
                rates = response.rates
            } catch (e: Exception) {
                error = "Failed to fetch rates: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
