package ru.kpfu.itis.galeev.android.myapplication.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import ru.kpfu.itis.galeev.android.myapplication.fragments.QuestionFragment
import ru.kpfu.itis.galeev.android.myapplication.utils.AllAnswersAreCompletedChecker
import ru.kpfu.itis.galeev.android.myapplication.utils.RecyclerViewViewPagerAdapter

class QuestionsAdapter(fragment : Fragment, private val list : MutableList<QuestionFragment>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        with(RecyclerViewViewPagerAdapter) {
            if (position == list.size - 1) {
                val allCompleted = AllAnswersAreCompletedChecker.check(
                    chosenAnswerInViewPagerFragments)

                list[position] = QuestionFragment.getInstance(
                    position + 1,
                    fragmentsAnswers[position],
                    chosenAnswerInViewPagerFragments[position],
                    true
                )
            } else {
                if (chosenAnswerInViewPagerFragments[position] != -1) {
                    list[position] = QuestionFragment.getInstance(
                        position + 1,
                        fragmentsAnswers[position],
                        chosenAnswerInViewPagerFragments[position]
                    )
                }
            }
        }
        return list[position]
    }

}