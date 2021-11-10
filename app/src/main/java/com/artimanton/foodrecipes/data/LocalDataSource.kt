package com.artimanton.foodrecipes.data

import com.artimanton.foodrecipes.data.database.RecipesDao
import com.artimanton.foodrecipes.data.database.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {
    suspend fun insertRecipes(recipesEntity: RecipesEntity){
        recipesDao.insertRecipes(recipesEntity)
    }

    fun readDatabase():Flow<List<RecipesEntity>>{
        return recipesDao.readRecipes()
    }
}