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

}