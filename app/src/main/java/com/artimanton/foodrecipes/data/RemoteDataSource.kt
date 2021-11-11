package com.artimanton.foodrecipes.data

import com.artimanton.foodrecipes.data.network.FoodRecipesApi
import com.artimanton.foodrecipes.models.FoodRecipe

import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi

){

    suspend fun getRecipes(queries: Map<String, String>):Response<FoodRecipe>{
        return foodRecipesApi.getRecipes(queries)
    }

}