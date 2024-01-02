package com.example.plantdiarybyemdad.service

import androidx.lifecycle.MutableLiveData
import com.example.plantdiarybyemdad.dto.Plant

class PlantService {
    fun fetchPlants(plantName:String):MutableLiveData<ArrayList<Plant>>{
        return MutableLiveData<ArrayList<Plant>>()
    }
}