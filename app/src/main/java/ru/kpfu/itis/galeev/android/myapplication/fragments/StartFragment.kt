package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
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


            // add [+7 (9] to the beginning
            textEditPhone.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && textEditPhone.length() == 0) {
                    textEditPhone.setText("+7 (9")
                    textEditPhone.setSelection(5)
                }
            }


            textEditPhone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (textEditPhone.text!!.length < 5) {
                        textEditPhone.setText("+7 (9")
                        textEditPhone.setSelection(5)
                        textEditPhone.addTextChangedListener(this)
                    }

                    if (!textEditPhone.text!!.startsWith('+')) {
                        textEditPhone.setText(s!!.substring(1, s.length))
                        textEditPhone.setSelection(s.length - 1)
                        textEditPhone.addTextChangedListener(this)
                    }

                    if (textEditPhone.text!!.length == 19) {
                        textEditPhone.setText(s!!.substring(0, start) + s.substring(start + 1, s.length))
                        textEditPhone.setSelection(s.length - 1)
                        textEditPhone.addTextChangedListener(this)

                    }

                    when (textEditPhone.text!!.length) {
                        7 -> {
                            if (start == 6) {
                                textEditPhone.setText(textEditPhone.text.toString() + ")-")
                                textEditPhone.setSelection(9)
                                textEditPhone.addTextChangedListener(this)
                            }
                        }
                        8 -> {
                            textEditPhone.setText(s!!.substring(0, 6))
                            textEditPhone.setSelection(6)
                            textEditPhone.addTextChangedListener(this)
                        }
                        12 -> {
                            if (start == 12) {
                                textEditPhone.setText(s!!.substring(0, 11))
                                textEditPhone.setSelection(11)
                                textEditPhone.addTextChangedListener(this)
                            } else {
                                textEditPhone.setText(textEditPhone.text.toString() + "-")
                                textEditPhone.setSelection(13)
                                textEditPhone.addTextChangedListener(this)
                            }
                        }
                        15 -> {
                            if (start == 15) {
                                textEditPhone.setText(s!!.substring(0, 14))
                                textEditPhone.setSelection(14)
                                textEditPhone.addTextChangedListener(this)
                            } else {
                                textEditPhone.setText(s!!.toString() + "-")
                                textEditPhone.addTextChangedListener(this)
                                textEditPhone.setSelection(16)
                            }
                        }
                        19 -> {
                            textEditPhone.setText(s!!.substring(0, 18))
                            textEditPhone.setSelection(18)
                            textEditPhone.addTextChangedListener(this)
                        }
                    }
                    btnTakeSurvey.isVisible = textEditPhone.text!!.length == 18 && !textEditQuestionsCount.text.toString().isBlank()
                }

                override fun afterTextChanged(s: Editable?) {}
            })


            textEditQuestionsCount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnTakeSurvey.isVisible = textEditPhone.text!!.length == 18 && !textEditQuestionsCount.text.toString().isBlank()
                }

                override fun afterTextChanged(s: Editable?) {}
            })

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