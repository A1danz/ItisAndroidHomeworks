package ru.kpfu.itis.galeev.android.myapplication.utils

import android.content.Context
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.model.AnswerData
import kotlin.random.Random

object AnswersGenerator {
    var _preparedAnswers : Array<String>? = null
    val preparedAnswers get() = _preparedAnswers!!
    fun generateAnswers(context : Context) : List<String> {
        if (_preparedAnswers == null) {
            _preparedAnswers = context.resources.getStringArray(R.array.answers)
        }

        val answerList = List(Random.nextInt(5, 10)) {
            preparedAnswers[Random.nextInt(0, preparedAnswers.size)]
        }

        return answerList
    }
}