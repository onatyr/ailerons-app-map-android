package fr.onat68.aileronsappmapandroid.data.repositories

import fr.onat68.aileronsappmapandroid.data.entities.IndividualDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndividualRepository @Inject constructor(private val individualDao: IndividualDAO) {

    fun getListIndividual() = individualDao.getAll()
}