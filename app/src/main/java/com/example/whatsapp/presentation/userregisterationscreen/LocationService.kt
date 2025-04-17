package com.example.whatsapp.presentation.userregisterationscreen

import retrofit2.http.GET
import retrofit2.http.Headers

interface LocationService {

    @GET("v1/countries")
    @Headers("X-CSCAPI-KEY: N3A0ZGk5aWNmdEh1dDZZOTlkbElLSmxYV0JMWDJaNWI0TkZuQ1hOYQ==")
    suspend fun getCountries(): List<Country>


}