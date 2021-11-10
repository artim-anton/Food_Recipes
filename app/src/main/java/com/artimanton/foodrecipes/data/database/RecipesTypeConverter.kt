package com.artimanton.foodrecipes.data.database

import androidx.room.TypeConverter
import com.artimanton.foodrecipes.models.FoodRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun foodRecipesToString(foodRecipe: FoodRecipe):String{
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
     fun stringToFoodRecipes(data: String): FoodRecipe{
         val listTipe = object : TypeToken<FoodRecipe>() {}.type
         return gson.fromJson(data, listTipe)
    }
}