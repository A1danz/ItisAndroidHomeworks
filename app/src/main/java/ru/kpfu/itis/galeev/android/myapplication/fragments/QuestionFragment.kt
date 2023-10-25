package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.adapter.AnswersAdapter
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentQuestionBinding
import ru.kpfu.itis.galeev.android.myapplication.model.AnswerData
import ru.kpfu.itis.galeev.android.myapplication.utils.ActionType
import ru.kpfu.itis.galeev.android.myapplication.utils.AllAnswersAreCompletedChecker
import ru.kpfu.itis.galeev.android.myapplication.utils.AnswersGenerator
import ru.kpfu.itis.galeev.android.myapplication.utils.ArgumentsNames
import ru.kpfu.itis.galeev.android.myapplication.utils.RecyclerViewViewPagerAdapter
import ru.kpfu.itis.galeev.android.myapplication.utils.SaveBtnShowUtil
import kotlin.properties.Delegates

class QuestionFragment : BaseFragment(R.layout.fragment_question) {
    var _viewBinding : FragmentQuestionBinding? = null
    val viewBinding : FragmentQuestionBinding get() = _viewBinding!!
    var rvAdapter : AnswersAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewBinding = FragmentQuestionBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewBinding.btnEndSurvey.visibility == View.INVISIBLE) {
            setEndBtnVisibility()
        }
        initViews()
    }

    override fun onResume() {
        super.onResume()
        if (viewBinding.btnEndSurvey.visibility == View.INVISIBLE) {
            setEndBtnVisibility()
        }
    }

    fun initViews() {
        with(viewBinding) {
            initEndBtn()

            val questionNumber = requireArguments().getInt(ArgumentsNames.questionNumber)
            tvQuestionNumber.text = "Вопрос $questionNumber"
            val answers  = arguments?.getStringArrayList(ArgumentsNames.answers)!!

            val chosenAnswer : Int
            if (arguments?.containsKey(ArgumentsNames.chosenAnswer)!!) {
                chosenAnswer = arguments?.getInt(ArgumentsNames.chosenAnswer)!!
            } else {
                chosenAnswer = -1
            }

            val answerDataList = mutableListOf<AnswerData>()
            for (index in 0 until answers.size) {
                answerDataList.add(AnswerData(answers[index], chosenAnswer == index))
            }

            rvAdapter = AnswersAdapter(answerDataList)
            rvQuestions.adapter = rvAdapter
        }
    }

    fun initEndBtn() {
        with (viewBinding) {
            btnEndSurvey.setOnClickListener {
                val allCompleted =SaveBtnShowUtil.allAreCompleted ||
                    AllAnswersAreCompletedChecker.check(RecyclerViewViewPagerAdapter.chosenAnswerInViewPagerFragments)
                if (allCompleted) {
                    Toast.makeText(requireContext(), "Вы успешно прошли опрос!", Toast.LENGTH_SHORT)
                        .show()
                    moveToStartScreen()
                    return@setOnClickListener
                }
                Toast.makeText(
                    requireContext(),
                    "Пожалуйста, ответьте на все вопросы",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun setEndBtnVisibility() {
        with(viewBinding) {
            val showBtn = SaveBtnShowUtil.allAreCompleted || requireArguments().containsKey(ArgumentsNames.isLastFragmentInVp)
            btnEndSurvey.visibility = if (showBtn) View.VISIBLE else View.INVISIBLE
        }
    }


    fun moveToStartScreen() {
        (requireActivity() as? BaseActivity)?.moveToScreen(
            actionType = ActionType.REPLACE,
            destination = StartFragment.newInstance(),
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
        rvAdapter = null
    }

    companion object {
        fun getInstance() = QuestionFragment()
        fun getInstance(questionNumber : Int, answers : List<String>) : QuestionFragment {
            val frag : QuestionFragment = getInstance()

            val bundle : Bundle = bundleOf().apply {
                putInt(ArgumentsNames.questionNumber, questionNumber)
                putStringArrayList(ArgumentsNames.answers, ArrayList<String>(answers))
            }

            frag.apply {
                arguments = bundle
            }
            return frag
        }
        fun getInstance(questionNumber : Int, answers : List<String>, chosenItem: Int) : QuestionFragment {
            val frag : QuestionFragment = getInstance()

            val bundle : Bundle = bundleOf().apply {
                putInt(ArgumentsNames.questionNumber, questionNumber)
                putStringArrayList(ArgumentsNames.answers, ArrayList<String>(answers))
                putInt(ArgumentsNames.chosenAnswer, chosenItem)
            }

            frag.apply {
                arguments = bundle
            }
            return frag
        }

        fun getInstance(questionNumber : Int, answers : List<String>, chosenItem: Int, isLastFragmentInVp : Boolean) : QuestionFragment {
            val frag : QuestionFragment = getInstance()
            val bundle : Bundle = bundleOf().apply {
                putInt(ArgumentsNames.questionNumber, questionNumber)
                putStringArrayList(ArgumentsNames.answers, ArrayList<String>(answers))
                putInt(ArgumentsNames.chosenAnswer, chosenItem)
                putBoolean(ArgumentsNames.isLastFragmentInVp, isLastFragmentInVp)
            }

            frag.apply {
                arguments = bundle
            }
            return frag
        }
    }
}