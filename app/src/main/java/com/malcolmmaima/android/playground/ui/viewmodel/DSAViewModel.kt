package com.malcolmmaima.android.playground.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import okio.utf8Size

class DSAViewModel: ViewModel() {

    /** Constant Time function
     * The size of names does not affect the running time of this function.
     * Whether names has 10 items or 10 million items, this function only
     * checks the first element of the list.
     * The big O notation for this function is O(1) where 1 is the constant time
     **/
    fun checkFirst(names: List<String>){
        var enterRunningTime = System.currentTimeMillis()
        if(names.firstOrNull() != null) {
            Log.d("DSAViewModel", "checkFirst: ${names.first()} - Time(${System.currentTimeMillis() - enterRunningTime}ms)")
           println(names.first())
        } else {
            Log.d("DSAViewModel", "checkFirst: no names - Time(${System.currentTimeMillis() - enterRunningTime}ms)")
        }
    }

    /**
     * Linear Time function
     * The size of names affects the running time of this function. As the size of names
     * increases, the running time increases linearly. The big O notation for this function
     * is O(n) where n is the size of names.
     */
    fun printNames(names: List<String>){
        var enterRunningTime = System.currentTimeMillis()
        for(name in names){
            Log.d("DSAViewModel", "printNames: $name - Time(${System.currentTimeMillis() - enterRunningTime}ms)")
            println(name)
        }
    }

    /**
     * Quadratic Time function
     * The size of names affects the running time of this function. As the size of names
     * increases, the running time increases quadratically. The big O notation for this function
     * is O(n^2) where n is the size of names. What this means is that if the size of names
     * doubles, the running time increases by 4 times.
     *
     * this n squared algorithm can quickly run out of control as the data size increases. Imagine printing 1000 names
     */
    fun printNamesTwice(names: List<String>) {
        Log.d("DSAViewModel", "printNamesTwice: ${String::class.java.simpleName.utf8Size()} bytes")
        var enterRunningTime = System.currentTimeMillis()
        for (name in names) {
            for (name2 in names) {
                Log.d(
                    "DSAViewModel",
                    "printNamesTwice: $name of $name2 Time(${System.currentTimeMillis() - enterRunningTime}ms)"
                )
                println(name)
            }
        }
    }

    /** Sort Algorithms **/
    fun bubbleSort(numbers: List<Int>): List<Int> {
        var enterRunningTime = System.currentTimeMillis()
        var sorted = false
        var list = numbers.toMutableList()
        while (!sorted) {
            sorted = true
            for (i in 0 until list.size - 1) {
                if (list[i] > list[i + 1]) {
                    sorted = false
                    val temp = list[i]
                    list[i] = list[i + 1]
                    list[i + 1] = temp
                }
            }
        }
        Log.d("DSAViewModel", "bubbleSort: Time(${System.currentTimeMillis() - enterRunningTime}ms)")
        return list
    }

    fun selectionSort(numbers: List<Int>): List<Int> {
        var enterRunningTime = System.currentTimeMillis()
        var list = numbers.toMutableList()
        for (i in 0 until list.size - 1) {
            var min = i
            for (j in i + 1 until list.size) {
                if (list[j] < list[min]) {
                    min = j
                }
            }
            val temp = list[i]
            list[i] = list[min]
            list[min] = temp
        }
        Log.d("DSAViewModel", "selectionSort: Time(${System.currentTimeMillis() - enterRunningTime}ms)")
        return list
    }

    fun insertionSort(numbers: List<Int>): List<Int> {
        var enterRunningTime = System.currentTimeMillis()
        var list = numbers.toMutableList()
        for (i in 1 until list.size) {
            val temp = list[i]
            var j = i
            while (j > 0 && list[j - 1] > temp) {
                list[j] = list[j - 1]
                j--
            }
            list[j] = temp
        }
        Log.d("DSAViewModel", "insertionSort: Time(${System.currentTimeMillis() - enterRunningTime}ms)")
        return list
    }

    fun mergeSort(numbers: List<Int>): List<Int> {
        var enterRunningTime = System.currentTimeMillis()
        if (numbers.size <= 1) {
            return numbers
        }
        val middle = numbers.size / 2
        var left = numbers.subList(0, middle)
        var right = numbers.subList(middle, numbers.size)
        left = mergeSort(left)
        right = mergeSort(right)
        var result = mutableListOf<Int>()
        while (left.isNotEmpty() && right.isNotEmpty()) {
            if (left.first() <= right.first()) {
                result.add(left.first())
                left = left.subList(1, left.size)
            } else {
                result.add(right.first())
                right = right.subList(1, right.size)
            }
        }
        while (left.isNotEmpty()) {
            result.add(left.first())
            left = left.subList(1, left.size)
        }
        while (right.isNotEmpty()) {
            result.add(right.first())
            right = right.subList(1, right.size)
        }
        Log.d("DSAViewModel", "mergeSort: Time(${System.currentTimeMillis() - enterRunningTime}ms)")
        return result
    }

    /**
     * Space Complexity
     */
    fun calculateSpaceComplexity(function: () -> Unit): Long {
        val runtime = Runtime.getRuntime()
        val usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory()
        function()
        val usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory()
        return usedMemoryAfter - usedMemoryBefore
    }

}