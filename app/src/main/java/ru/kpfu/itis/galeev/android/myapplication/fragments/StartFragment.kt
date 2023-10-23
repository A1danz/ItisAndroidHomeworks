package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentStartBinding
import ru.kpfu.itis.galeev.android.myapplication.utils.ActionType

class StartFragment : BaseFragment(R.layout.fragment_start) {
    var _viewBinding : FragmentStartBinding? = null
    val viewBinding : FragmentStartBinding get() = _viewBinding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = FragmentStartBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun initViews() {
        with (viewBinding) {
            btnTakeSurvey.isVisible = !textEditPhone.text.toString().isBlank() && !textEditQuestionsCount.text.toString().isBlank()

            val textWatcher : TextWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnTakeSurvey.isVisible = !textEditPhone.text.toString().isBlank() && !textEditQuestionsCount.text.toString().isBlank()
                }
                override fun afterTextChanged(s: Editable?) {}
            }
            textEditPhone.addTextChangedListener(textWatcher)
            textEditQuestionsCount.addTextChangedListener(textWatcher)

            btnTakeSurvey.setOnClickListener {
                if (textEditQuestionsCount.text?.toString()?.toInt() == 0) {
                    Toast.makeText(requireContext(), "Нельзя создать опросник без вопросов", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                (requireActivity() as? BaseActivity)?.moveToScreen(
                    ActionType.REPLACE,
                    QuestionnaireFragment.newInstance(textEditQuestionsCount.text.toString().toInt())
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
    companion object {
        fun newInstance() = StartFragment()
    }
}