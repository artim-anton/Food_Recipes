package com.artimanton.foodrecipes.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.artimanton.foodrecipes.models.FoodRecipe
import com.artimanton.foodrecipes.util.Constrans.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}