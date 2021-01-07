package com.adams.laralasaskriva

object Settings {

    const val NOT_SET = ""
    const val FOREST = "forest"
    const val FARM = "farm"
    const val FOOD = "food"


    //Figure out a way to get string resources
    private val categories = arrayListOf(
        Category(FOOD, R.string.foodTitle, R.drawable.background_food, R.color.foodBackground)
    )

    var currentGameMode = NOT_SET

    fun getCategoryColor(category: String = currentGameMode): Int {
        return when (category) {
            NOT_SET -> R.color.white
            FOREST -> R.color.forestBackground
            FARM -> R.color.farmBackground
            FOOD -> R.color.foodBackground
            else -> R.color.white
        }
    }

    fun getAllCategories(): ArrayList<Category> {
        return categories
    }
}