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
package com.malcolmmaima.android.playground.di

import com.malcolmmaima.android.playground.data.repository.WeatherRepository
import com.malcolmmaima.android.playground.data.repository.WeatherRepositoryImpl
import com.malcolmmaima.android.playground.ui.viewmodel.DSAViewModel
import com.malcolmmaima.android.playground.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val repositoryModule: Module = module {
    single<WeatherRepository> { WeatherRepositoryImpl(weatherAPI = get()) }
}

val viewModelModule: Module = module {
    viewModel { MainViewModel(weatherRepository = get()) }
    viewModel { DSAViewModel()}
}

val appModules: List<Module> = listOf(
    viewModelModule,
    repositoryModule
)