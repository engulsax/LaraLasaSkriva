package com.adams.laralasaskriva

import android.graphics.drawable.Drawable

data class LearningWord(
    val word: String,
    val level: Int,
    val imageId: Int,
    val soundId: Int,
    val length: Int = word.length
) {}