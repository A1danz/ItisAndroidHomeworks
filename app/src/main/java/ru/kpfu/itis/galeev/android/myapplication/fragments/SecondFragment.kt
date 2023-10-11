package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentSecondBinding
import ru.kpfu.itis.galeev.android.myapplication.utils.ActionType
import ru.kpfu.itis.galeev.android.myapplication.utils.ParamsKey

class SecondFragment : BaseFragment(R.layout.fragment_second) {
    private var _viewBinding : FragmentSecondBinding? = null
    private val viewBinding : FragmentSecondBinding
        get() = _viewBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewBinding = FragmentSecondBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun initViews() {
        val textFromFirstScreen = arguments?.getString(ParamsKey.givenText)
        if (!textFromFirstScreen.isNullOrEmpty()) {
            viewBinding.tvSecondScreenText.text = textFromFirstScreen
        }

        viewBinding.btnGo1Fragment.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        viewBinding.btnGo3Fragment.setOnClickListener {
            parentFragmentManager.popBackStack()
            (requireActivity() as? BaseActivity)?.moveToScreen(
                ActionType.REPLACE,
                ThirdFragment.getInstance(textFromFirstScreen.toString()),
                Configuration.ORIENTATION_PORTRAIT,
                ThirdFragment.THIRD_FRAGMENT_TAG,
                true
            )
        }
    }

    companion object {
        const val SECOND_FRAGMENT_TAG = "SECOND_FRAGMENT_TAG"

        fun getInstance(text: String): SecondFragment {
            val bundle: Bundle = Bundle().apply {
                putString(ParamsKey.givenText, text)
            }
            val fragment = getInstance().apply {
                arguments = bundle
            }
            return fragment
        }

        fun getInstance() = SecondFragment()
    }

    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }
}