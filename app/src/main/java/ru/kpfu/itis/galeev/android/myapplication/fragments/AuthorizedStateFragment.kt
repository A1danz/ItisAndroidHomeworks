package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.AuthorizedStateFragmentBinding

class AuthorizedStateFragment : BaseFragment(R.layout.authorized_state_fragment) {
    var _viewBinding : AuthorizedStateFragmentBinding? = null
    val viewBidning : AuthorizedStateFragmentBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = AuthorizedStateFragmentBinding.inflate(inflater)
        return viewBidning.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBidning) {
            val navHost = childFragmentManager.findFragmentById(R.id.auth_state_container) as NavHostFragment
            val navController = navHost.navController
            bnv.setupWithNavController(navController)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

}