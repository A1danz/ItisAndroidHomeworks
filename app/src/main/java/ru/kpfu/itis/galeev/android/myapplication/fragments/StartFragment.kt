package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentStartBinding
import ru.kpfu.itis.galeev.android.myapplication.utils.ActionType

class StartFragment : BaseFragment(R.layout.fragment_start) {
    private var _viewBinding : FragmentStartBinding? = null
    private val viewBinding get() = _viewBinding!!
    private var toastIsActive : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewBinding = FragmentStartBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun initViews() {
        with (viewBinding) {
            editTextCars.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s!!.length > 2) {
                        Toast.makeText(requireContext(), "Максимальное количество: 45", Toast.LENGTH_SHORT).show()
                        editTextCars.setText(s.substring(0, 2))
                        editTextCars.setSelection(2)
                        editTextCars.removeTextChangedListener(this)
                        editTextCars.addTextChangedListener(this)
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            btnShowCars.setOnClickListener {
                if (editTextCars.text!!.isBlank()) {
                    Toast.makeText(requireContext(), "Заполните поле!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val carsCount : Int = editTextCars.text.toString().toInt()
                if (carsCount > 45) {
                    Toast.makeText(requireContext(), "Максимальное количество: 45", Toast.LENGTH_SHORT).show()
                } else if (carsCount < 0) {
                    Toast.makeText(requireContext(), "Отрицательные значения не допустимы", Toast.LENGTH_SHORT).show()
                } else {
                    (requireActivity() as? BaseActivity)?.moveToScreen(
                        ActionType.REPLACE,
                        CarsFragment.getInstance(carsCount),
                        CarsFragment.CARS_FRAGMENT_TAG
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    companion object {
        val START_FRAGMENT_TAG = "START_FRAGMENT_TAG"
    }
}