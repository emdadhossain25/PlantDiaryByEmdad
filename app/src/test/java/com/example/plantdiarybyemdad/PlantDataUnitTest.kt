package com.example.plantdiarybyemdad

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.plantdiarybyemdad.dto.Plant
import com.example.plantdiarybyemdad.ui.main.MainViewModel
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
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var mvm: MainViewModel

    @Test
    fun confirmEasternRedbud_outputsEasternRedbud() {
        var plant: Plant = Plant("Cercis", "canadesis", "Eastern Redbud")
        assertEquals("Eastern Redbud", plant.toString())
    }


    @Test
    fun searchForRedBud_returnsRedBud() {
        givenFeedOfPlantDataAreAvailable()
        whenSearchForRedbud()
        thenResultContainingEasternRedbud()
    }

    private fun givenFeedOfPlantDataAreAvailable() {
        mvm = MainViewModel()
    }

    private fun whenSearchForRedbud() {
        mvm.fetchPlants("Redbud")
    }

    private fun thenResultContainingEasternRedbud() {
        var redbudFound = false

        mvm.plants.observeForever {
            assertNotNull(it)
            assertTrue(it.size() > 0)
            it.forEach {
                if (it.genus == "Cercis" && it.species == "canadensis" && it.common.contains("Eastern Redbud")) {
                    redbudFound = true;
                }
            }
        }
        assertTrue(redbudFound)
    }


    @Test
    fun searchForGarbage_returnsNothing() {
        givenFeedOfPlantDataAreAvailable()
        whenISearchForGarbage()
        thenIGetZeroResults()

    }

    private fun whenISearchForGarbage() {
        mvm.fetchPlants("sklujapouetllkjsdau")

    }

    private fun thenIGetZeroResults() {
        mvm.plants.observeForever {
            assertEquals(0, it.size)
        }
    }
}