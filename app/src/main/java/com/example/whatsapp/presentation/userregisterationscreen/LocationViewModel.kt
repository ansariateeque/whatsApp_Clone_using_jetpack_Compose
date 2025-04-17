package com.example.whatsapp.presentation.userregisterationscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(private val repository: LocationRepository) :
    ViewModel() {


    private val _countryList = MutableStateFlow<List<Country>>(emptyList())
    val countryList: StateFlow<List<Country>> = _countryList

    private val _selectedCountry = MutableStateFlow<Country?>(null)
    val selectedCountry: StateFlow<Country?> = _selectedCountry

    init {
        viewModelScope.launch {
            _countryList.value = repository.fetchCountries()
        }
    }

    fun selectCountry(country: Country) {
        _selectedCountry.value = country
    }

}