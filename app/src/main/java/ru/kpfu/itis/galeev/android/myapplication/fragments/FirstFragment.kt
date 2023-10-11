package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentFirstBinding
import ru.kpfu.itis.galeev.android.myapplication.utils.ActionType
import ru.kpfu.itis.galeev.android.myapplication.utils.ParamsKey

class FirstFragment : BaseFragment(R.layout.fragment_first) {
    private var _viewBinding: FragmentFirstBinding? = null
    private val viewBinding: FragmentFirstBinding
        get() = _viewBinding!!
    private var dequeWords = ArrayDeque<String>()
    private var dequeIsFilled : Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewBinding = FragmentFirstBinding.inflate(inflater)
        return viewBinding.root
    }

    fun initViews() {
        with(viewBinding) {
            val orientation: Int = arguments?.getInt(ParamsKey.orientation)!!
            println("Orientation in firstScreen is - " + orientation)


            btnGo3Fragment.setOnClickListener {
                (requireActivity() as? BaseActivity)?.moveToScreen(
                    actionType = ActionType.REPLACE,
                    destination = SecondFragment.getInstance(viewBinding.textInputTypeText.text.toString()),
                    Configuration.ORIENTATION_PORTRAIT,
                    SecondFragment.SECOND_FRAGMENT_TAG,
                    true
                )
                (requireActivity() as? BaseActivity)?.moveToScreen(
                    actionType = ActionType.REPLACE,
                    destination = ThirdFragment.getInstance(viewBinding.textInputTypeText.text.toString()),
                    Configuration.ORIENTATION_PORTRAIT,
                    ThirdFragment.THIRD_FRAGMENT_TAG,
                    true
                )
            }
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                saveBtn.visibility = View.VISIBLE
                println("${dequeWords.size} - deque words count")


                saveBtn.setOnClickListener {
                    val userInputText = textInputTypeText.text.toString()
                    textInputTypeText.text?.clear()

                    if (dequeWords.size == 3) {
                        if (!dequeIsFilled) {
                            Toast.makeText(
                                requireActivity(),
                                "Далее будут сохраняться последние 3 результата",
                                Toast.LENGTH_LONG)
                                .show()
                            dequeIsFilled = true
                        }
                        dequeWords.removeFirst()
                    }
                    dequeWords.addLast(userInputText)
                    println(dequeWords.joinToString("\n"))
                    println("text from textinput - $userInputText")
                    (requireActivity() as? BaseActivity)?.moveToScreen(
                        actionType = ActionType.REPLACE,
                        destination = FourthFragment.getInstance(dequeWords.joinToString("\n")),
                        Configuration.ORIENTATION_LANDSCAPE,
                        FourthFragment.FOURTH_FRAGMENT_TAG,
                        false
                    )
                }

                textInputTypeText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        saveBtn.isEnabled = !s.isNullOrEmpty()
                    }

                    override fun afterTextChanged(s: Editable?) {}
                })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }

    companion object {
        val FIRST_FRAGMENT_TAG = "FIRST_FRAGMENT_TAG"
        fun getInstance() = FirstFragment()
        fun getInstance(orientation : Int) : FirstFragment {
            val bundle : Bundle = bundleOf().apply {
                putInt(ParamsKey.orientation, orientation)
            }

            val fragment = FirstFragment.getInstance()
            fragment.apply {
                arguments = bundle
            }

            return fragment
        }
    }

}