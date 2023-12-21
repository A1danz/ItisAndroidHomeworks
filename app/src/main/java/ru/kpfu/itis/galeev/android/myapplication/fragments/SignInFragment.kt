package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.AuthFragment
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.data.db.dao.UserDao
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.UserEntity
import ru.kpfu.itis.galeev.android.myapplication.databinding.SignInFragmentBinding
import ru.kpfu.itis.galeev.android.myapplication.db.service.UserService
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator
import ru.kpfu.itis.galeev.android.myapplication.utils.AuthorizationUtil
import ru.kpfu.itis.galeev.android.myapplication.utils.RegexPatterns
import ru.kpfu.itis.galeev.android.myapplication.utils.UXMessages

class SignInFragment : AuthFragment(R.layout.sign_in_fragment) {
    private var _viewBinding : SignInFragmentBinding? = null;
    private val viewBinding : SignInFragmentBinding get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId : Int? = getUserIdFromSession(AuthorizationUtil(ServiceLocator.getDbInstance(requireContext()).userDao))
        if (userId != null) {
            ServiceLocator.authorizeUser(userId)
            findNavController().navigate(R.id.action_signInFragment_to_songsListFragment)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = SignInFragmentBinding.inflate(inflater);

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews();
    }

    fun initViews() {
        val userService = UserService(ServiceLocator.getDbInstance(requireContext()).userDao)
        val authUtil = AuthorizationUtil(ServiceLocator.getDbInstance(requireContext()).userDao)
        val alertBuilder = AlertDialog.Builder(requireContext())
        with(viewBinding) {
            btnSignIn.setOnClickListener {
                val email : String = editTextEmail.text.toString()
                val password : String = editTextPassword.text.toString()
//                val authResult = UserService.checkUserData(email, password)
                if (!email.matches(RegexPatterns.EMAIL_REGEX)) {
                    editTextEmail.setError(getString(R.string.email_valid_error))
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val authResult = authUtil.authorizeUser(email, password)
                        withContext(Dispatchers.Main) {
                            if (authResult == UXMessages.SUCCESS_AUTH) {
                                if (cbRememberMe.isChecked) {
                                    launch(Dispatchers.IO) {
                                        initSession(ServiceLocator.getUserId(), authUtil)
                                    }
                                }
                                findNavController().navigate(R.id.action_signInFragment_to_songsListFragment)
                            } else {
                                alertBuilder.setMessage("Вы ввели неправильный номер телефона/пароль")
                                alertBuilder.show()
                            }
                        }
                    }
                }
            }
            btnMoveToRegister.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}