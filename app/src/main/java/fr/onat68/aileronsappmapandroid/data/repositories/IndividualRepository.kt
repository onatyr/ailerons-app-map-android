package fr.onat68.aileronsappmapandroid.data.repositories

import fr.onat68.aileronsappmapandroid.data.entities.FavoriteDAO
import fr.onat68.aileronsappmapandroid.data.entities.IndividualDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndividualRepository @Inject constructor(private val individualDao: IndividualDAO, private val favoriteDao: FavoriteDAO) {

    suspend fun getListIndividual() = individualDao.getAll()

    fun getListFavorite() = individualDao.getListFavorite()
    fun addFavorite(id: Int) = individualDao.addFavorite(id)
    fun removeFavorite(id: Int) = individualDao.removeFavorite(id)
}