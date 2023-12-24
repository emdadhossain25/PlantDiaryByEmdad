package com.example.plantdiarybyemdad

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun addition_isNotCorrect(){
        assertEquals(3,1+2)
    }

    @Test
    fun addTwoAndThree_EqualFive() {
        assertEquals(5,2+3)
    }

    @Test
    fun addThreeAndThree_isEqualSix() {
        assertEquals(6,3+3)
    }

    @Test
    fun confirmEasternRedbud_outputsEasternRedbud(){
        var plant:Plant = Plant("Cercis","canadesis","Eastern Redbud")
        assertEquals("Eastern Redbud",plant.toString())
    }
}