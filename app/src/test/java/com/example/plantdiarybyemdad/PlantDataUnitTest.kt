package com.example.plantdiarybyemdad

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.plantdiarybyemdad.dto.Plant
import com.example.plantdiarybyemdad.service.PlantService
import com.example.plantdiarybyemdad.ui.main.MainViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PlantDataUnitTest {
    @get:Rule
    var rule: TestRule =
        InstantTaskExecutorRule() // this will make testing live data observables a little bit easier for using observeForever
    lateinit var mvm: MainViewModel

    var plantService = mockk<PlantService>()

    @Test
    fun confirmEasternRedbud_outputsEasternRedbud() {
        var plant: Plant = Plant("Cercis", "canadesis", "Eastern Redbud")
        assertEquals("Eastern Redbud", plant.toString())
    }

    @Test
    fun searchForRedbud_returnsRedbud() {
        givenAFeedOfMockedPlantDataAreAvailable()
        whenSearchForRedbud()
        thenResultContainsEasternRedbud()
        thenVerifyFunctionsInvoked()
    }

    private fun thenVerifyFunctionsInvoked() {
        verify { plantService.fetchPlants("Redbud") } // was called verify whenSearchForRedbud() called plantSevice.fetchplants("Redbud")
        verify(exactly = 0) { plantService.fetchPlants("Maple") } // was never called
        confirmVerified(plantService) // confirm recorded calls are verified correctly
    }

    private fun givenAFeedOfMockedPlantDataAreAvailable() {
        mvm = MainViewModel()
        createMockData()
    }

    private fun createMockData() {
        var allPlantsLiveData = MutableLiveData<ArrayList<Plant>>()
        var allPlants = ArrayList<Plant>()
        // create and add plants to our collection.
        var redbud = Plant("Cercis", "canadencis", "Eastern Redbud")
        allPlants.add(redbud)

        var redOak = Plant("Quercus", "rubra", "Red Oak")
        allPlants.add(redOak)

        var whiteOak = Plant("Quercus", "alba", "White Oak")
        allPlants.add(whiteOak)

        allPlantsLiveData.postValue(allPlants)
        every { plantService.fetchPlants(or("Redbud", "Quercus")) } returns allPlantsLiveData // redbud or qeurcus return allplantsLiveData
        every {
            plantService.fetchPlants(not(or("Redbud", "Quercus")))
        } returns MutableLiveData<ArrayList<Plant>>() // anything but not redbud or quercus return empty live data
        mvm.plantService = plantService
    }

    private fun whenSearchForRedbud() {
        mvm.fetchPlants("Redbud")
    }

    private fun thenResultContainsEasternRedbud() {
        var redbudFound = false
        mvm.plants.observeForever {
            assertNotNull(it) // did we get something back?
            assertTrue(it.size > 0) // did we get a collection back? and is that collection not empty?
            it.forEach {
                if (it.genus == "Cercis" && it.species == "canadencis" && it.common.contains("Eastern Redbud")) {
                    redbudFound = true
                }
            }
        }
        assertTrue(redbudFound)
    }

    @Test
    fun searchForGarbage_returnsNothing() {
        givenAFeedOfMockedPlantDataAreAvailable()
        whenISearchForGarbage()
        thenIGetZeroResult()
    }

    private fun whenISearchForGarbage() {
        mvm.fetchPlants("kjalsjdflajdf")
    }

    private fun thenIGetZeroResult() {
        mvm.plants.observeForever {
            assertEquals(0, it.size)
        }
    }
}
