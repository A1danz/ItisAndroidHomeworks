package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentBottomSheetBinding
import ru.kpfu.itis.galeev.android.myapplication.utils.ActionType

class BottomSheetFragment : BottomSheetDialogFragment(R.layout.fragment_bottom_sheet) {
    private var _viewBinding : FragmentBottomSheetBinding? = null
    private val viewBinding : FragmentBottomSheetBinding get() = _viewBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewBinding = FragmentBottomSheetBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun initViews() {
        with(viewBinding) {
            editTextCars.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (editTextCars.text != null && editTextCars.text!!.length > 1) {
                        editTextCars.setText(s!!.substring(0, 1))
                        editTextCars.setSelection(1)
                        Toast.makeText(requireContext(),"Максимальное количество новостей - 5", Toast.LENGTH_SHORT)
                            .show()
                        editTextCars.removeTextChangedListener(this)
                        editTextCars.addTextChangedListener(this)
                    }
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            btnAddCars.setOnClickListener {
                if (editTextCars.text == null || editTextCars.text!!.isBlank()) {
                    Toast.makeText(requireContext(), "Заполните поле с количеством машин", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val carsCount = editTextCars.text.toString().toInt()
                    if (carsCount <= 0 || carsCount > 5) {
                        Toast.makeText(requireContext(), "Значение должно быть в диапазоне от 0 до 5", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val carsFrag : Fragment = requireActivity().supportFragmentManager.findFragmentByTag(CarsFragment.CARS_FRAGMENT_TAG)!!
                        (carsFrag as? CarsFragment)!!.apply {
                            this.addCars(carsCount)
                        }
                        this@BottomSheetFragment.dismiss()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    companion object {
        val BOTTOM_SHEET_FRAGMENT = "BOTTOM_SHEET_FRAGMENT"
    }
}