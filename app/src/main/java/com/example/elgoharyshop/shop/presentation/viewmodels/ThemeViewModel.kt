package com.example.elgoharyshop.shop.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elgoharyshop.shop.data.AppDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val appDataStore: AppDataStore
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow<Boolean?>(null)
    val isDarkMode: StateFlow<Boolean?> = _isDarkMode

    init {
        viewModelScope.launch {
            val initial = appDataStore.getInitialDarkMode()
            _isDarkMode.value = initial
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val newMode = !(_isDarkMode.value ?: false)
            appDataStore.setDarkMode(newMode)
            _isDarkMode.value = newMode
        }
    }
}
