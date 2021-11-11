package com.artimanton.foodrecipes.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.artimanton.foodrecipes.data.Repository
import com.artimanton.foodrecipes.data.database.RecipesEntity
import com.artimanton.foodrecipes.models.FoodRecipe
import com.artimanton.foodrecipes.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application):AndroidViewModel(application) {

    /** ROOM DATABASE */
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    /** RETROFIT */
    val recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String,String>) = viewModelScope.launch {
        getRecipesSaveCall(queries)
    }

    private suspend fun getRecipesSaveCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
                try {
                    val response = repository.remote.getRecipes(queries)
                    recipesResponse.value = handleFoodRecipesResponse(response)

                    val foodRecipe = recipesResponse.value!!.data
                    if (foodRecipe != null) {
                        offlineCacheRecipes(foodRecipe)
                    }
                }catch (e: Exception){
                    recipesResponse.value = NetworkResult.Error("Recipes not found.")
                }
        }else{
            recipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)

    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection():Boolean{
            val connectivityManeger = getApplication<Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val activeNetwork = connectivityManeger.activeNetwork ?: return false
            val capbilities = connectivityManeger.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capbilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capbilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capbilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                else -> return false

            }
        }
}