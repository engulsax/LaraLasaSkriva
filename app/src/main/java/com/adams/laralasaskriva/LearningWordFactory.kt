package com.adams.laralasaskriva

import android.app.ListActivity
import kotlin.random.Random

object Levels {
    val LEVEL_1 = 1
    val LEVEL_2 = 2
    val LEVEL_3 = 3
    val ALL_LEVELS = 0
}

val alphabet = arrayListOf(
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O'
    , 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Å', 'Ä', 'Ö'
)

class LearningWordFactory() {

    fun getWords(numOfWords: Int, level: Int): ArrayList<LearningWord> {
        val words = ArrayList<LearningWord>()
        for (x in 0 until numOfWords) {
            words.add(learningWordsFood[x])
        }
        return words
    }

    fun getRandomWord(level: Int): LearningWord {
        return learningWordsFood[ Random.nextInt(0, learningWordsFood.size - 1)]
    }

    fun getWordAsCharList(word: String): ArrayList<Char> {
        val letters = ArrayList<Char>()
        for (letter in word) {
            letters.add(letter)
        }
        return letters
    }

    fun getRandomLetters(numOfLetters: Int): ArrayList<Char> {
        val randomLetters = ArrayList<Char>()
        for (x in 0 until numOfLetters) {
            randomLetters.add(alphabet[ Random.nextInt(0, alphabet.size - 1)])
        }
        return randomLetters
    }

    private val learningWordsFood = arrayOf(
        LearningWord("KORV", Levels.LEVEL_2, R.drawable.korv_png, R.raw.korv_mp3),
        LearningWord("GLASS", Levels.LEVEL_3, R.drawable.glass_png, R.raw.glass_mp3),
        LearningWord("OST", Levels.LEVEL_1, R.drawable.ost_png, R.raw.ost_mp3),
        LearningWord("MJÖLK", Levels.LEVEL_3, R.drawable.mjolk_png, R.raw.mjolk_mp3),
        LearningWord("BANAN", Levels.LEVEL_3, R.drawable.banan_png, R.raw.banan_mp3),
        LearningWord("SYLT", Levels.LEVEL_2, R.drawable.sylt_png, R.raw.sylt_mp3),
        LearningWord("ÄPPLE", Levels.LEVEL_3, R.drawable.apple_png, R.raw.apple_mp3),
        LearningWord("ÄGG", Levels.LEVEL_1, R.drawable.agg_png, R.raw.agg_mp3),
        LearningWord("PÄRON", Levels.LEVEL_3, R.drawable.paron_png, R.raw.paron_mp3),
        LearningWord("GURKA", Levels.LEVEL_3, R.drawable.gurka_png, R.raw.gurka_mp3),
        LearningWord("BRÖD", Levels.LEVEL_2, R.drawable.brod_png, R.raw.brod_mp3),
        LearningWord("MAJS", Levels.LEVEL_2, R.drawable.majs_png, R.raw.majs_mp3),
        LearningWord("LÖK", Levels.LEVEL_1, R.drawable.lok_png, R.raw.lok_mp3),
        LearningWord("PUMPA", Levels.LEVEL_2, R.drawable.pumpa_png, R.raw.pumpa_mp3),
        LearningWord("GRÖT", Levels.LEVEL_2, R.drawable.grot_png, R.raw.grot_mp3),
        LearningWord("PASTA", Levels.LEVEL_3, R.drawable.pasta_png, R.raw.pasta_mp3)
    )

    /*private val learningWordsLvl1 = arrayOf(
        LearningWord("GRIS", Levels.LEVEL_1, R.drawable.pig),
        LearningWord("RÄV", Levels.LEVEL_1, R.drawable.rav),
        LearningWord("REN", Levels.LEVEL_1, R.drawable.ren),
        LearningWord("ÄLG", Levels.LEVEL_1, R.drawable.alg),
        LearningWord("HÄST", Levels.LEVEL_1, R.drawable.hast),
        LearningWord("FÅR", Levels.LEVEL_1, R.drawable.far),
        LearningWord("VARG", Levels.LEVEL_1, R.drawable.varg),
        LearningWord("KO", Levels.LEVEL_1, R.drawable.ko)
    )*/


    /*private val learningWordsLvl2 = arrayOf(
        LearningWord()
    )

    private val learningWordsLvl3 = arrayOf(
        LearningWord()
    )*/
}

