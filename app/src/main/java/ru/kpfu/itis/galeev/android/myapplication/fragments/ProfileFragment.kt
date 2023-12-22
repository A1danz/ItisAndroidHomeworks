package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.ProfileFragmentBinding
import ru.kpfu.itis.galeev.android.myapplication.db.service.UserService
import ru.kpfu.itis.galeev.android.myapplication.di.ServiceLocator
import ru.kpfu.itis.galeev.android.myapplication.model.UserModel
import ru.kpfu.itis.galeev.android.myapplication.utils.RegexPatterns
import ru.kpfu.itis.galeev.android.myapplication.utils.UXMessages
import kotlin.text.StringBuilder

class ProfileFragment : BaseFragment(R.layout.profile_fragment) {
    var _viewBinding : ProfileFragmentBinding? = null
    val viewBinding : ProfileFragmentBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = ProfileFragmentBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

        val userService : UserService = UserService(ServiceLocator.getDbInstance(requireContext()).userDao)
        val userId : Int = ServiceLocator.getUserId()
        var userModel : UserModel? = null
        lifecycleScope.launch(Dispatchers.IO) {
            userModel = userService.getUserById(userId)
            withContext(Dispatchers.Main) {
                updateInterfaceByUserModel(userModel!!)
            }
        }
        val alertBuilder = AlertDialog.Builder(requireContext())
        with(viewBinding) {
            editTextChangePhoneNumber.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus && editTextChangePhoneNumber.length() == 0) {
                    editTextChangePhoneNumber.setText("+7 (9")
                    editTextChangePhoneNumber.setSelection(5)
                }
            }
            editTextChangePhoneNumber.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (editTextChangePhoneNumber.text!!.length < 5) {
                        editTextChangePhoneNumber.setText("+7 (9")
                        editTextChangePhoneNumber.setSelection(5)
                        editTextChangePhoneNumber.addTextChangedListener(this)
                    }

                    if (!editTextChangePhoneNumber.text!!.startsWith('+')) {
                        editTextChangePhoneNumber.setText(s!!.substring(1, s.length))
                        editTextChangePhoneNumber.setSelection(s.length - 1)
                        editTextChangePhoneNumber.addTextChangedListener(this)
                    }

                    if (editTextChangePhoneNumber.text!!.length == 19) {
                        editTextChangePhoneNumber.setText(s!!.substring(0, start) + s.substring(start + 1, s.length))
                        editTextChangePhoneNumber.setSelection(s.length - 1)
                        editTextChangePhoneNumber.addTextChangedListener(this)

                    }

                    when (editTextChangePhoneNumber.text!!.length) {
                        7 -> {
                            if (start == 6) {
                                editTextChangePhoneNumber.setText(editTextChangePhoneNumber.text.toString() + ")-")
                                editTextChangePhoneNumber.setSelection(9)
                                editTextChangePhoneNumber.addTextChangedListener(this)
                            }
                        }
                        8 -> {
                            editTextChangePhoneNumber.setText(s!!.substring(0, 6))
                            editTextChangePhoneNumber.setSelection(6)
                            editTextChangePhoneNumber.addTextChangedListener(this)
                        }
                        12 -> {
                            if (start == 12) {
                                editTextChangePhoneNumber.setText(s!!.substring(0, 11))
                                editTextChangePhoneNumber.setSelection(11)
                                editTextChangePhoneNumber.addTextChangedListener(this)
                            } else {
                                editTextChangePhoneNumber.setText(editTextChangePhoneNumber.text.toString() + "-")
                                editTextChangePhoneNumber.setSelection(13)
                                editTextChangePhoneNumber.addTextChangedListener(this)
                            }
                        }
                        15 -> {
                            if (start == 15) {
                                editTextChangePhoneNumber.setText(s!!.substring(0, 14))
                                editTextChangePhoneNumber.setSelection(14)
                                editTextChangePhoneNumber.addTextChangedListener(this)
                            } else {
                                editTextChangePhoneNumber.setText(s!!.toString() + "-")
                                editTextChangePhoneNumber.addTextChangedListener(this)
                                editTextChangePhoneNumber.setSelection(16)
                            }
                        }
                        19 -> {
                            editTextChangePhoneNumber.setText(s!!.substring(0, 18))
                            editTextChangePhoneNumber.setSelection(18)
                            editTextChangePhoneNumber.addTextChangedListener(this)
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            etNewPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (etRepeatPassword.error != null) {
                        etRepeatPassword.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            etRepeatPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (etNewPassword.error != null) {
                        etRepeatPassword.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            btnSaveChanges.setOnClickListener {
                updateInfoAboutUser(userId, userService)
            }

            btnExit.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    clearSession()
                    withContext(Dispatchers.Main) {
                        ServiceLocator.terminateUserAuth()
                        Navigation.findNavController(
                            requireActivity(),
                            R.id.main_activity_container
                        ).navigate(R.id.action_authorizedStateFragment_to_signInFragment)
                    }
                }
            }
            btnDeleteProfile.setOnClickListener {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setMessage(getString(R.string.are_you_sure_want_delete_proifle))
                alertDialog.setPositiveButton(getString(R.string.yes)) { dialog, which -> deleteProfile(userId, userService) }
                alertDialog.setNegativeButton(getString(R.string.no)) { dialog, which -> }
                alertDialog.show()
            }

        }
    }

    private fun updateInfoAboutUser(userId : Int, userService: UserService) {
        with(viewBinding) {
            val phoneChanged = editTextPhoneNumber.text.toString() != editTextChangePhoneNumber.text.toString()
            var newPhone : String? = null
            if (phoneChanged) {
                if (editTextChangePhoneNumber.text.toString().matches(RegexPatterns.PHONE_REGEX)) {
                    newPhone = editTextChangePhoneNumber.text.toString()
                }
            }

            val currentPassFilled = etCurrentPassword.text.isNotEmpty()
            val newPassFilled = etNewPassword.text.isNotEmpty()
            val repeatNewPassFilled = etRepeatPassword.text.isNotEmpty()
            val passFieldsChanged = currentPassFilled || newPassFilled || repeatNewPassFilled
            println("TEST TAG - $currentPassFilled, $newPassFilled, $repeatNewPassFilled, $passFieldsChanged")
            var passwordChanged = false
            if (passFieldsChanged) {
                if (currentPassFilled && newPassFilled && repeatNewPassFilled) {
                    if (etNewPassword.text.toString() == etRepeatPassword.text.toString()) {
                        passwordChanged = true
                    } else {
                        etNewPassword.error = getString(R.string.passwords_not_equals)
                        etRepeatPassword.error = getString(R.string.passwords_not_equals)
                    }
                } else {
                    if (!currentPassFilled) etCurrentPassword.error = getString(R.string.fill_field)
                    if (!newPassFilled) etNewPassword.error = getString(R.string.fill_field)
                    if (!repeatNewPassFilled) etRepeatPassword.error =
                        getString(R.string.fill_field)
                }
            }
            if (newPhone == null && !passwordChanged) return
            lifecycleScope.launch(Dispatchers.IO) {
                val result : StringBuilder = StringBuilder()
                var phoneChangeResultDeferred : Deferred<Result<Enum<UXMessages>>>? = null
                var passwordChangeResultDeferred : Deferred<Result<Enum<UXMessages>>>? = null
                if (newPhone != null) phoneChangeResultDeferred = async { updatePhone(userId, userService, newPhone) }
                if (passwordChanged) passwordChangeResultDeferred = async {
                    changePassword(
                        userId,
                        userService,
                        etCurrentPassword.text.toString(),
                        etNewPassword.text.toString()
                    )
                }
                if (phoneChangeResultDeferred != null) {
                    val phoneChangeResult = phoneChangeResultDeferred.await()
                    if (phoneChangeResult.isSuccess) {
                        phoneChangeResult.getOrNull()!!.let { msg ->
                            if (msg == UXMessages.SUCCESS_CHANGE_PHONE) {
                                result.append(getString(R.string.sucess_change_phone))
                                updatePhoneNumber(userService)
                            } else {
                                result.append(getString(R.string.failure_change_phone))
                            }
                        }
                    } else {
                        println("TEST TAG - Exc while change phone - ${phoneChangeResult.exceptionOrNull()}")
                        result.append(getString(R.string.error_while_change_phone))
                    }
                    result.append("\n")
                }
                if (passwordChangeResultDeferred != null) {
                    val passwordChangeResult = passwordChangeResultDeferred.await()
                    if (passwordChangeResult.isSuccess) {
                        passwordChangeResult.getOrNull()?.let { msg ->
                            if (msg == UXMessages.PASSWORD_NOT_CORRECT) {
                                result.append(getString(R.string.failure_change_password))
                            } else {
                                result.append(getString(R.string.success_change_password))
                                withContext(Dispatchers.Main) {
                                    clearPassFields()
                                }
                            }
                        }
                    } else {
                        println("TEST TAG fail change password - ${passwordChangeResult.exceptionOrNull()}")
                        result.append(getString(R.string.error_while_change_password))
                    }
                }
                withContext(Dispatchers.Main) {
                    val alertBuilder = AlertDialog.Builder(requireContext())
                    alertBuilder.setMessage(result.toString())
                    alertBuilder.show()
                }
            }
        }
    }

    fun updatePhone(userId : Int, userService: UserService, newPhone : String) : Result<Enum<UXMessages>> {
//        ServiceLocator.getDbInstance(requireContext()).userDao.updatePhoneNumber(userId, newPhone)
        return kotlin.runCatching {
            userService.changeUserPhoneNumber(userId, newPhone)
        }
    }

    fun deleteProfile(userId : Int, userService: UserService) {
        lifecycleScope.launch(Dispatchers.IO) {
            userService.deleteUser(userId)
            clearSession()
            withContext(Dispatchers.Main) {
                ServiceLocator.terminateUserAuth()
                Navigation.findNavController(
                    requireActivity(),
                    R.id.main_activity_container
                ).navigate(R.id.action_authorizedStateFragment_to_signInFragment)
            }
        }

    }

    fun changePassword(
        userId: Int,
        userService: UserService,
        currentPassword: String,
        newPassword: String
    ): Result<Enum<UXMessages>> {
        return kotlin.runCatching {
            userService.changeUserPassword(userId, currentPassword, newPassword)
        }
    }

    fun updateInterfaceByUserModel(userModel: UserModel) {
        with(viewBinding) {
            editTextName.setText(userModel.name)
            editTextEmail.setText(userModel.email)
                editTextPhoneNumber.setText(userModel.phoneNumber)
                editTextChangePhoneNumber.setText(userModel.phoneNumber)
        }
    }

    fun updatePhoneNumber(userService: UserService) {
        lifecycleScope.launch(Dispatchers.IO) {
            val user = userService.getUserById(ServiceLocator.getUserId())
            withContext(Dispatchers.Main) {
                viewBinding.editTextPhoneNumber.setText(user.phoneNumber)
                viewBinding.editTextChangePhoneNumber.setText(user.phoneNumber)
            }
        }
    }

    fun clearPassFields() {
        with(viewBinding) {
            etCurrentPassword.setText("")
            etNewPassword.setText("")
            etRepeatPassword.setText("")
        }
    }

    fun clearSession() {
        lifecycleScope.launch {
            val sp = requireActivity().getPreferences(Context.MODE_PRIVATE)
            with(sp.edit()) {
                remove(getString(R.string.session_arg_name))
                apply()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}