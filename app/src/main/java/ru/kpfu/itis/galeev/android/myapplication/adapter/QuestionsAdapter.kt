package ru.kpfu.itis.galeev.android.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import ru.kpfu.itis.galeev.android.myapplication.fragments.QuestionFragment
import ru.kpfu.itis.galeev.android.myapplication.model.QuestionData
import ru.kpfu.itis.galeev.android.myapplication.utils.AllAnswersAreCompletedChecker
import ru.kpfu.itis.galeev.android.myapplication.utils.RecyclerViewViewPagerAdapter
import ru.kpfu.itis.galeev.android.myapplication.utils.SaveBtnShowUtil

class QuestionsAdapter(fragment : Fragment, private val questionsList : MutableList<QuestionData>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return questionsList.size + 2
    }

    override fun createFragment(position: Int): Fragment {
//      calc realPosition for circularViewPager
        var realPosition : Int = position
        if (position == 0) {
            realPosition = questionsList.size - 1
        } else if (position == itemCount - 1) {
            realPosition = 0
        } else {
            realPosition -= 1
        }

        with(RecyclerViewViewPagerAdapter) {
            if (SaveBtnShowUtil.lastIsComplete) {
                SaveBtnShowUtil.allAreCompleted = SaveBtnShowUtil.allAreCompleted ||
                        AllAnswersAreCompletedChecker.check(chosenAnswerInViewPagerFragments)
            }

            // show last fragment with 100% visibility endBtn
            if (realPosition == questionsList.size - 1) {
                SaveBtnShowUtil.lastIsComplete = true
                return QuestionFragment.getInstance(
                    realPosition + 1,
                    questionsList[realPosition].answers,
                    chosenAnswerInViewPagerFragments[realPosition],
                    true
                )
            } else {
                // very very simple optimization
                if (chosenAnswerInViewPagerFragments[realPosition] != -1) {
                   return QuestionFragment.getInstance(
                       realPosition + 1,
                       questionsList[realPosition].answers,
                       chosenAnswerInViewPagerFragments[realPosition],
                    )
                }
            }
        }
        return QuestionFragment.getInstance(
            realPosition + 1,
            questionsList[realPosition].answers
        )
    }

}