package fr.onat68.aileronsappmapandroid.data.repositories

import fr.onat68.aileronsappmapandroid.data.entities.Individual
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndividualRepository @Inject constructor(private val individualDao: IndividualDAO) {

    fun getListIndividual() = individualDao.getAll()

    suspend fun insertIndividual(individual: Individual) = individualDao.insert(individual)

    suspend fun clearIndividual() = individualDao.deleteAll()

    fun getListFavorite() = individualDao.getListFavorite()
    suspend fun addToFavorite(id: Int) = individualDao.addToFavorite(id)
    suspend fun removeFromFavorite(id: Int) = individualDao.removeFromFavorite(id)
}