package com.artimanton.foodrecipes.di

import android.content.Context
import androidx.room.Dao
import androidx.room.Room
import com.artimanton.foodrecipes.data.database.RecipesDatabase
import com.artimanton.foodrecipes.util.Constrans.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        RecipesDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(recipesDatabase: RecipesDatabase) = recipesDatabase.recipesDao()
}