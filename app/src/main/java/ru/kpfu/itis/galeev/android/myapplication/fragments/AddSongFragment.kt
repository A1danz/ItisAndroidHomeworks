package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.AddSongFragmentBinding

class AddSongFragment : BaseFragment(R.layout.add_song_fragment) {
    var _viewBinding : AddSongFragmentBinding? = null
    val viewBinding : AddSongFragmentBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = AddSongFragmentBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}