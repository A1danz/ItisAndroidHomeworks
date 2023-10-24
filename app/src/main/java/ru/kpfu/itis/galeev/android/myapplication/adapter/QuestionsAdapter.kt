package ru.kpfu.itis.galeev.android.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import ru.kpfu.itis.galeev.android.myapplication.fragments.QuestionFragment
import ru.kpfu.itis.galeev.android.myapplication.model.QuestionData
import ru.kpfu.itis.galeev.android.myapplication.utils.AllAnswersAreCompletedChecker
import ru.kpfu.itis.galeev.android.myapplication.utils.RecyclerViewViewPagerAdapter

class QuestionsAdapter(fragment : Fragment, private val questionsList : MutableList<QuestionData>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return questionsList.size
    }

    override fun createFragment(position: Int): Fragment {
        with(RecyclerViewViewPagerAdapter) {
            if (position == questionsList.size - 1) {
                return QuestionFragment.getInstance(
                    position + 1,
                    questionsList[position].answers,
                    chosenAnswerInViewPagerFragments[position],
                    true
                )
            } else {
                if (chosenAnswerInViewPagerFragments[position] != -1) {
                   return QuestionFragment.getInstance(
                        position + 1,
                        questionsList[position].answers,
                        chosenAnswerInViewPagerFragments[position]
                    )
                }
            }
        }

        return QuestionFragment.getInstance(
            position + 1,
            questionsList[position].answers
        )
    }
}