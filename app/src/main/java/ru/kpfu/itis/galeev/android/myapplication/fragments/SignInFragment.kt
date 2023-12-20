package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.SignInFragmentBinding
import ru.kpfu.itis.galeev.android.myapplication.db.service.UserService
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator

class SignInFragment : BaseFragment(R.layout.sign_in_fragment) {
    private var _viewBinding : SignInFragmentBinding? = null;
    private val viewBinding : SignInFragmentBinding get() = _viewBinding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = SignInFragmentBinding.inflate(inflater);

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews();
    }

    fun initViews() {
        with(viewBinding) {
            btnSignIn.setOnClickListener {
                val email : String = editTextEmail.text.toString()
                val password : String = editTextPassword.text.toString()
                val authResult = UserService.checkUserData(email, password)
                lifecycleScope.launch(Dispatchers.IO) {
                    ServiceLocator.getDbInstance(ctx = requireContext())
                }
            }
        }
    }
}