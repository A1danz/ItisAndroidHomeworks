package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.adapter.AnswersAdapter
import ru.kpfu.itis.galeev.android.myapplication.adapter.QuestionsAdapter
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentQuestionnaireBinding
import ru.kpfu.itis.galeev.android.myapplication.model.AnswerData
import ru.kpfu.itis.galeev.android.myapplication.model.QuestionData
import ru.kpfu.itis.galeev.android.myapplication.utils.AnswersGenerator
import ru.kpfu.itis.galeev.android.myapplication.utils.ArgumentsNames
import ru.kpfu.itis.galeev.android.myapplication.utils.EnableCircularPager
import ru.kpfu.itis.galeev.android.myapplication.utils.RecyclerViewViewPagerAdapter
import ru.kpfu.itis.galeev.android.myapplication.utils.SaveBtnShowUtil

class QuestionnaireFragment : BaseFragment(R.layout.fragment_questionnaire) {
    var _viewBinding : FragmentQuestionnaireBinding? = null
    val viewBinding : FragmentQuestionnaireBinding get() = _viewBinding!!
    var vpAdapter : QuestionsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = FragmentQuestionnaireBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()

    }

    private fun initViewPager() {
        val questionCount : Int = requireArguments().getInt(ArgumentsNames.questionCount)

        val chosenAnswersInViewPagerFragments : MutableList<Int> = mutableListOf()
        for (index in 0 until questionCount) {
            chosenAnswersInViewPagerFragments.add(-1)
        }
        RecyclerViewViewPagerAdapter.chosenAnswerInViewPagerFragments = chosenAnswersInViewPagerFragments

        val questions : MutableList<QuestionData> = mutableListOf()
        for (index in 0 until questionCount) {
            val generatedAnswers = AnswersGenerator.generateAnswers(requireContext())
            questions.add(QuestionData(generatedAnswers))
        }

        with(viewBinding) {
            vpAdapter = QuestionsAdapter(this@QuestionnaireFragment, questions.toMutableList())
            vp2Questions.adapter = vpAdapter

            // initialize CurcularViewPager
            vp2Questions.setCurrentItem(1, false)
            EnableCircularPager.enableCircularPager(vp2Questions)


            // update ViewPagerPosition
            vp2Questions.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        RecyclerViewViewPagerAdapter.viewPagerPosition = vpAdapter!!.itemCount - 2
                    } else if (position == vpAdapter!!.itemCount - 1) {
                        RecyclerViewViewPagerAdapter.viewPagerPosition = 0
                    } else {
                        RecyclerViewViewPagerAdapter.viewPagerPosition = position - 1
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
        vpAdapter = null
        RecyclerViewViewPagerAdapter.clearVariables()
        SaveBtnShowUtil.clearVariables()
    }

    companion object {
        fun newInstance() = QuestionnaireFragment()
        fun newInstance(questionCount : Int) : QuestionnaireFragment {
            val frag : QuestionnaireFragment = QuestionnaireFragment()

            val bundle : Bundle = bundleOf().apply {
                putInt(ArgumentsNames.questionCount, questionCount)
            }
            frag.apply {
                arguments = bundle
            }

            return frag
        }
    }
}