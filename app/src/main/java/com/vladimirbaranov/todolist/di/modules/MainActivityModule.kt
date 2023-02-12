package com.vladimirbaranov.todolist.di.modules

import com.vladimirbaranov.todolist.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(
        modules = [
            FragmentBuildersModule::class
        ]
    )
    abstract fun provideMainActivity(): MainActivity
}