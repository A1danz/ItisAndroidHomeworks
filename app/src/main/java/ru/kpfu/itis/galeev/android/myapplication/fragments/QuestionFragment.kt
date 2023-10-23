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
        initViews()
    }

    fun initViews() {
        with(viewBinding) {
            val questionNumber = requireArguments().getInt(ArgumentsNames.questionNumber)
            tvQuestionNumber.text = "Вопрос $questionNumber"
            val answers  = arguments?.getStringArrayList(ArgumentsNames.answers)!!

            val chosenAnswer : Int
            if (arguments?.containsKey(ArgumentsNames.chosenAnswer)!!) {
                chosenAnswer = arguments?.getInt(ArgumentsNames.chosenAnswer)!!
            } else {
                chosenAnswer = -1
            }

            if (arguments?.containsKey(ArgumentsNames.isLastFragmentInVp)!!) {
                initEndBtn()
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
        with(viewBinding) {
            btnEndSurvey.visibility = View.VISIBLE

            btnEndSurvey.setOnClickListener {
                val allCompleted = AllAnswersAreCompletedChecker.check(RecyclerViewViewPagerAdapter.chosenAnswerInViewPagerFragments)
                if (allCompleted) {
                    Toast.makeText(requireContext(), "Вы успешно прошли опрос!", Toast.LENGTH_SHORT).show()
                    moveToStartScreen()
                    return@setOnClickListener
                }
                Toast.makeText(requireContext(), "Пожалуйста, ответьте на все вопросы", Toast.LENGTH_SHORT).show()
            }
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

            println("DEBUG TAG Deafult Instance")
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

            println("DEBUG TAG NoDeafult Instance")
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

            println("DEBUG TAG NoDeafult Instance")
            return frag
        }
    }

}