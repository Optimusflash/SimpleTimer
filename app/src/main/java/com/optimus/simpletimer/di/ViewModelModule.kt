package com.optimus.simpletimer.di

import androidx.lifecycle.ViewModel
import com.optimus.simpletimer.viewmodels.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Dmitriy Chebotar on 11.09.2020.
 */

@Module
abstract class ViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(MainViewModel::class)
    abstract fun provideMainViewModel(mainViewModel: MainViewModel): ViewModel

}