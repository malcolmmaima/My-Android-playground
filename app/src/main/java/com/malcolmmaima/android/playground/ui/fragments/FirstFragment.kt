/*
 * Copyright 2022 Malcolm Maima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.malcolmmaima.android.playground.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.DialogCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.malcolmmaima.android.playground.R
import com.malcolmmaima.android.playground.databinding.FragmentFirstBinding
import com.malcolmmaima.android.playground.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.malcolmmaima.android.playground.ui.viewmodel.DSAViewModel
import com.network.data.models.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val mainViewModel: MainViewModel by inject()
    private val dsaViewModel: DSAViewModel by inject()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var names = mutableListOf<String>()
    var numbers = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        // initialize a list of n number names and pass to the DSA view model
        generateNames()
        return binding.root

    }

    private fun generateNames(size: Int = 100) {
        names.clear()
        for (i in 1..size) {
            names.add("Name $i")
        }
    }

    private fun generateNumbers(size: Int = 100) {
        numbers.clear()
        for (i in 1..size) {
            numbers.add(i)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchWeatherData()
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        _binding?.buttonFirst?.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Select Algorithm")
            alertDialogBuilder.setItems(
                arrayOf("Constant Time", "Linear Time", "Quadratic Time", "Other")
            ) { _, which ->
                when (which) {
                    0 -> {
                        // Constant Time
                        generateNames()
                        dsaViewModel.checkFirst(names)
                        Log.d("FirstFragment", "Space complexity: ${dsaViewModel.calculateSpaceComplexity { dsaViewModel.checkFirst(names) }}")
                    }
                    1 -> {
                        // Linear Time
                        generateNames()
                        dsaViewModel.printNames(names)
                        Log.d("FirstFragment", "Space complexity: ${dsaViewModel.calculateSpaceComplexity { dsaViewModel.printNames(names) }}")
                    }
                    2 -> {
                        // Quadratic Time
                        generateNames(2)
                        dsaViewModel.printNamesTwice(names)
                        Log.d("FirstFragment", "Space complexity: ${dsaViewModel.calculateSpaceComplexity { dsaViewModel.printNamesTwice(names) }}")
                    }

                    3 -> {
                        // Other
                        generateNumbers()
                        dsaViewModel.bubbleSort(numbers)
                        Log.d("FirstFragment", "Space complexity: ${dsaViewModel.calculateSpaceComplexity { dsaViewModel.bubbleSort(numbers) }}")
                    }
                }
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mainViewModel.weatherData.collect { temperature ->
                    binding.textViewToday.text =  "${temperature.today.lowTemp} - ${temperature.today.highTemp} ??C"
                    binding.textViewTodaySmall.text = "${temperature.today.description}"


                    binding.textViewTomorrow.text = "${temperature.tomorrow.lowTemp} - ${temperature.today.highTemp} ??C"
                    binding.textViewTomorrowSmall.text = "${temperature.tomorrow.description}"

                    binding.textViewAfterTomorrow.text = "${temperature.dayAfterTomorrow.lowTemp} - ${temperature.dayAfterTomorrow.highTemp} ??C"
                    binding.textViewAfterTomorrowSmall.text = "${temperature.dayAfterTomorrow.description}"
                    changeIcon(temperature)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mainViewModel.errorMessage.collectLatest {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun changeIcon(temperature: WeatherResponse) {
        //change icon for today, tomorrow and day after tomorrow

        // create a hashmap containing the icon and the corresponding image resource e.g. "CLEAR_DAY" -> R.drawable.clear_day
        // use the icon from the response to get the corresponding image resource
        // set the image resource to the image view

        val iconMap = hashMapOf(
            "CLEAR_DAY" to R.drawable.ic_weather_clear_day,
            "CLEAR_NIGHT" to R.drawable.ic_weather_clear_night,
            "PARTLY_CLOUDY_DAY" to R.drawable.ic_weather_some_clouds,
            "SCATTERED_CLOUDS_DAY" to R.drawable.ic_weather_some_clouds,
            "BROKEN_OVERCAST_CLOUDS_DAY" to R.drawable.ic_weather_some_clouds,
            "SUNNY" to R.drawable.ic_weather_cloud_sun,
            "RAIN" to R.drawable.ic_weather_rain,
            "SNOW" to R.drawable.ic_weather_snow,
            "FOG" to R.drawable.ic_weather_fog,
            "DRIZZLE" to R.drawable.ic_weather_drizzle_some_rain,
        )

        binding.imageViewToday.setImageResource(iconMap[temperature.today.icon] ?: R.drawable.ic_weather_clear_day)
        binding.imageViewTomorrow.setImageResource(iconMap[temperature.tomorrow.icon] ?: R.drawable.ic_weather_clear_day)
        binding.imageViewAfterTomorrow.setImageResource(iconMap[temperature.dayAfterTomorrow.icon] ?: R.drawable.ic_weather_clear_day)

    }

    private fun fetchWeatherData(){
        mainViewModel.getWeatherData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}