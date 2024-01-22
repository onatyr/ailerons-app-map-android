package fr.onat68.aileronsappmapandroid.individual

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import fr.onat68.aileronsappmapandroid.species.Individual

@Composable
fun IndividualScreen(individualsList: List<Individual>, listId: Int) {
    val individual = individualsList[listId]
    Column {
        Text("Name : ${individual.individualName}")
        Text("Age : ${individual.age}")
    }
}