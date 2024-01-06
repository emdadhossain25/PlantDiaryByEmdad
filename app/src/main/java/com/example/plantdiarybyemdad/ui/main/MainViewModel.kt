package com.example.plantdiarybyemdad.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.plantdiarybyemdad.dto.Plant
import com.example.plantdiarybyemdad.service.PlantService

class MainViewModel : ViewModel() {
    var plants: MutableLiveData<ArrayList<Plant>> = MutableLiveData<ArrayList<Plant>>()
    var plantService:PlantService = PlantService()

    init {
        fetchPlants("e")
    }
    fun fetchPlants(plantName: String) {
        plants=plantService.fetchPlants(plantName)
    }
}