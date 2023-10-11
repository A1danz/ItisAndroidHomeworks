package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentThirdBinding
import ru.kpfu.itis.galeev.android.myapplication.utils.ParamsKey

class ThirdFragment : BaseFragment(R.layout.fragment_third) {
    private var _viewBinding: FragmentThirdBinding? = null
    private val viewBinding : FragmentThirdBinding
        get() = _viewBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewBinding = FragmentThirdBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun initViews() {
        val textFromFirstScreen = arguments?.getString(ParamsKey.givenText)
        if (!textFromFirstScreen.isNullOrEmpty()) {
            viewBinding.tvThirdScreenText.text = arguments?.getString(ParamsKey.givenText)
        }
    }

    companion object {
        const val THIRD_FRAGMENT_TAG = "THIRD_FRAGMENT_TAG"

        fun getInstance(text : String) : ThirdFragment {
            val bundle : Bundle = Bundle().apply {
                putString(ParamsKey.givenText, text)
            }
            val fragment = getInstance().apply {
                arguments = bundle
            }
            return fragment
        }

        fun getInstance() = ThirdFragment()
    }

    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }
}