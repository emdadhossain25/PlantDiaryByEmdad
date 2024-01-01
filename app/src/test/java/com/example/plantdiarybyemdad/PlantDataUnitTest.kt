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
    var rule:TestRule= InstantTaskExecutorRule() // this will make testing live data observables a little bit easier
    lateinit var mvm:MainViewModel


    @Test
    fun confirmEasternRedbud_outputsEasternRedbud() {
        var plant: Plant = Plant("Cercis", "canadesis", "Eastern Redbud")
        assertEquals("Eastern Redbud", plant.toString())
    }

    @Test
    fun searchForRedbud_returnsRedbud(){
        givenAFeedOfPlantDataAreAvailable()
        whenSearchForRedbud()
        thenResultContainsEasternRedbud()
    }

    private fun givenAFeedOfPlantDataAreAvailable() {
        mvm = MainViewModel()
    }

    private fun whenSearchForRedbud() {
        mvm.fetchPlants("Redbud")
    }

    private fun thenResultContainsEasternRedbud() {
        var redbudFound=false
        mvm.plants.observeForever{
            assertNotNull(it) // did we get something back?
            assertTrue(it.size()>0) // did we get a collection back? and is that collection not empty?
            it.forEach{
                if(it.genus=="Cercis" && it.species=="canadencis" && it.common.contains("Eastern Redbud")){
                    redbudFound=true
                }
            }
        }
        assertTrue(redbudFound)
    }


    @Test
    fun searchForGarbage_returnsNothing(){
        givenAFeedOfPlantDataAreAvailable()
        whenISearchForGarbage()
        thenIGetZeroResult()
    }

    private fun whenISearchForGarbage() {
        mvm.fetchPlants("kjalsjdflajdf")
    }

    private fun thenIGetZeroResult() {
        mvm.plants.observeForever{
            assertEquals(0,it.size)
        }
    }


}