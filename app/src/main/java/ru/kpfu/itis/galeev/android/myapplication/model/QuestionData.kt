package ru.kpfu.itis.galeev.android.myapplication.model

data class QuestionData(
    val number : Int,
    val answers: List<AnswerData>,
    var selectedAnswer : Boolean = false
)
