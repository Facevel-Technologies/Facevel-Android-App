package com.facevel.inc.app.di

import android.content.Context
import com.facevel.inc.app.ui.fragment.Loader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object Misc2Module {
    @Provides
    @FragmentScoped
    fun providesLoader(@ActivityContext context: Context): Loader {
        return Loader(context)
    }

}