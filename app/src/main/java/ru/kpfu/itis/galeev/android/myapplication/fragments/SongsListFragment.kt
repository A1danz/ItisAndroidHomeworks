package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.SongsListFragmentBinding
import ru.kpfu.itis.galeev.android.myapplication.db.service.UserService
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator
import ru.kpfu.itis.galeev.android.myapplication.model.UserModel

class SongsListFragment : BaseFragment(R.layout.songs_list_fragment) {
    var _viewBinding : SongsListFragmentBinding? = null
    val viewBinding : SongsListFragmentBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = SongsListFragmentBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val userService = UserService(ServiceLocator.getDbInstance(requireContext()).userDao)
        with(viewBinding) {
            lifecycleScope.launch(Dispatchers.IO) {
                val user : UserModel = userService.getUserById(ServiceLocator.getUserId())
                withContext(Dispatchers.Main) {
                    tvInfo.text = user.toString()
                }
            }
            tvInfo
        }

    }
}