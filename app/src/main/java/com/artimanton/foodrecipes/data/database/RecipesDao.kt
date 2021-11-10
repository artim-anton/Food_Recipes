package com.artimanton.foodrecipes.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity) {}

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    abstract fun readRecipes():Flow<List<RecipesEntity>>
}