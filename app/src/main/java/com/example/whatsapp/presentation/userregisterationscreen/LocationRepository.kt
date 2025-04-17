package com.example.whatsapp.presentation.userregisterationscreen

import javax.inject.Inject

class LocationRepository @Inject constructor(private val api: LocationService) {

   suspend fun fetchCountries(): List<Country> {
        return api.getCountries()
    }

}