package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.AuthFragment
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.UserEntity
import ru.kpfu.itis.galeev.android.myapplication.databinding.SignUpFragmentBinding
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator
import ru.kpfu.itis.galeev.android.myapplication.utils.AuthorizationUtil
import ru.kpfu.itis.galeev.android.myapplication.utils.RegexPatterns
import ru.kpfu.itis.galeev.android.myapplication.utils.UXMessages

class SignUpFragment : AuthFragment(R.layout.sign_up_fragment) {
    var _viewBinding : SignUpFragmentBinding? = null
    val viewBinding :  SignUpFragmentBinding get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userId : Int? = getUserIdFromSession(AuthorizationUtil(ServiceLocator.getDbInstance(requireContext()).userDao))
        if (userId != null) {
            ServiceLocator.authorizeUser(userId)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = SignUpFragmentBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val authUtil = AuthorizationUtil(ServiceLocator.getDbInstance(requireContext()).userDao)
        val alertBuilder = AlertDialog.Builder(requireContext())
        with(viewBinding) {
            editTextPhoneNumber.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && editTextPhoneNumber.length() == 0) {
                    editTextPhoneNumber.setText("+7 (9")
                    editTextPhoneNumber.setSelection(5)
                }
            }


            editTextPhoneNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (editTextPhoneNumber.text!!.length < 5) {
                        editTextPhoneNumber.setText("+7 (9")
                        editTextPhoneNumber.setSelection(5)
                        editTextPhoneNumber.addTextChangedListener(this)
                    }

                    if (!editTextPhoneNumber.text!!.startsWith('+')) {
                        editTextPhoneNumber.setText(s!!.substring(1, s.length))
                        editTextPhoneNumber.setSelection(s.length - 1)
                        editTextPhoneNumber.addTextChangedListener(this)
                    }

                    if (editTextPhoneNumber.text!!.length == 19) {
                        editTextPhoneNumber.setText(s!!.substring(0, start) + s.substring(start + 1, s.length))
                        editTextPhoneNumber.setSelection(s.length - 1)
                        editTextPhoneNumber.addTextChangedListener(this)

                    }

                    when (editTextPhoneNumber.text!!.length) {
                        7 -> {
                            if (start == 6) {
                                editTextPhoneNumber.setText(editTextPhoneNumber.text.toString() + ")-")
                                editTextPhoneNumber.setSelection(9)
                                editTextPhoneNumber.addTextChangedListener(this)
                            }
                        }
                        8 -> {
                            editTextPhoneNumber.setText(s!!.substring(0, 6))
                            editTextPhoneNumber.setSelection(6)
                            editTextPhoneNumber.addTextChangedListener(this)
                        }
                        12 -> {
                            if (start == 12) {
                                editTextPhoneNumber.setText(s!!.substring(0, 11))
                                editTextPhoneNumber.setSelection(11)
                                editTextPhoneNumber.addTextChangedListener(this)
                            } else {
                                editTextPhoneNumber.setText(editTextPhoneNumber.text.toString() + "-")
                                editTextPhoneNumber.setSelection(13)
                                editTextPhoneNumber.addTextChangedListener(this)
                            }
                        }
                        15 -> {
                            if (start == 15) {
                                editTextPhoneNumber.setText(s!!.substring(0, 14))
                                editTextPhoneNumber.setSelection(14)
                                editTextPhoneNumber.addTextChangedListener(this)
                            } else {
                                editTextPhoneNumber.setText(s!!.toString() + "-")
                                editTextPhoneNumber.addTextChangedListener(this)
                                editTextPhoneNumber.setSelection(16)
                            }
                        }
                        19 -> {
                            editTextPhoneNumber.setText(s!!.substring(0, 18))
                            editTextPhoneNumber.setSelection(18)
                            editTextPhoneNumber.addTextChangedListener(this)
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            btnSignUp.setOnClickListener {
                var dataIsValid = true
                val name = editTextName.text.toString()
                val phoneNumber = editTextPhoneNumber.text.toString()
                val email = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()
                val repeatPassword = editTextRepeatPassword.text.toString()
                if (!name.matches(RegexPatterns.NAME_REGEX)) {
                    editTextName.setError(getString(R.string.name_valid_error))
                    dataIsValid = false
                }
                if (!phoneNumber.matches(RegexPatterns.PHONE_REGEX)) {
                    editTextPhoneNumber.setError(getString(R.string.phone_valid_error))
                    dataIsValid = false
                }
                if (!email.matches(RegexPatterns.EMAIL_REGEX)) {
                    editTextEmail.setError(getString(R.string.email_valid_error))
                    dataIsValid = false
                }
                if (password.isEmpty() || password != repeatPassword) {
                    editTextPassword.setError(getString(R.string.passwords_not_equals))
                    editTextRepeatPassword.setError(getString(R.string.passwords_not_equals))
                    dataIsValid = false
                }
                if (dataIsValid) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val result  = authUtil.registerUser(UserEntity(
                            null,
                            name,
                            phoneNumber,
                            email,
                            password
                        ))
                        withContext(Dispatchers.Main) {
                            if (result == UXMessages.SUCCESS_REGISTER) {
                                if (cbRememberMe.isChecked) {
                                    launch(Dispatchers.IO) {
                                        initSession(ServiceLocator.getUserId(), authUtil)
                                    }
                                }
                                findNavController().navigate(R.id.action_signUpFragment_to_authorizedStateFragment)
                            } else {
                                alertBuilder.setMessage(getString(R.string.user_already_exists_msg))
                                val alert = alertBuilder.create()
                                alert.show()
                            }
                        }
                    }
                }
            }

            editTextPassword.addTextChangedListener (
                object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (editTextRepeatPassword.error != null) editTextRepeatPassword.error = null
                    }

                    override fun afterTextChanged(s: Editable?) {}
                }
            )

            editTextRepeatPassword.addTextChangedListener (
                object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (editTextPassword.error != null) editTextPassword.error = null
                    }

                    override fun afterTextChanged(s: Editable?) {}
                }
            )

            btnMoveToLogin.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}