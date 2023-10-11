package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentFourthBinding
import ru.kpfu.itis.galeev.android.myapplication.utils.ParamsKey

class FourthFragment : BaseFragment(R.layout.fragment_fourth) {
    var _viewBinding : FragmentFourthBinding? = null
    val viewBinding : FragmentFourthBinding
        get() = _viewBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewBinding = FragmentFourthBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val text : String = arguments?.getString(ParamsKey.givenTextForFourthScreen).toString()

        viewBinding.tvFourthScreenTextView.text = text
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    companion object {
        val FOURTH_FRAGMENT_TAG = "FOURTH_FRAGMENT_TAG"
        fun getInstance() = FourthFragment()
        fun getInstance(text : String) : FourthFragment {
            val bundle : Bundle = bundleOf().apply {
                putString(ParamsKey.givenTextForFourthScreen, text)
            }

            val fragment = FourthFragment.getInstance()
            fragment.apply {
                arguments = bundle
            }

            return fragment
        }
    }
}