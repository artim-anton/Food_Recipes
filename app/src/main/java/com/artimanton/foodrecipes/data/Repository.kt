package com.artimanton.foodrecipes.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSorce: RemoteDataSorce,
    localDataSource: LocalDataSource
) {
    val remote = remoteDataSorce
    val local = localDataSource
}