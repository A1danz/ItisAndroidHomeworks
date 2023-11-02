package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.adapter.RVCarsAdapter
import ru.kpfu.itis.galeev.android.myapplication.base.BaseActivity
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentCarsBinding
import ru.kpfu.itis.galeev.android.myapplication.model.Car
import ru.kpfu.itis.galeev.android.myapplication.utils.ActionType
import ru.kpfu.itis.galeev.android.myapplication.utils.ArgumentNames
import ru.kpfu.itis.galeev.android.myapplication.utils.CarsGenerator
import ru.kpfu.itis.galeev.android.myapplication.utils.SimpleLocalStorage
import kotlin.random.Random

class CarsFragment : BaseFragment(R.layout.fragment_cars) {
    private var _viewBinding : FragmentCarsBinding? = null
    private val viewBinding get() = _viewBinding!!
    private var rvCarsAdapter : RVCarsAdapter? = null
    private var cars : MutableList<Car>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewBinding = FragmentCarsBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        println("DEBUG TAG - viewCreated")
    }

    @SuppressLint("ResourceType")
    fun initViews() {
        with(viewBinding) {
            val carsCount : Int = arguments?.getInt(ArgumentNames.CARS_COUNT)!!
            if (cars == null) {
                cars = CarsGenerator.generateCars(carsCount)
            }

            rvCarsAdapter = RVCarsAdapter(
                context = requireContext(),
                onItemStarClicked = ::onItemStartClicked,
                onItemClicked = ::onItemClicked,
                onBtnClicked = ::onBtnClicked
            )

            rvCarsAdapter!!.setItems(cars!!)
            rvCars.adapter = rvCarsAdapter
            if (cars!!.size > 12) {
                val gridLayoutManager : GridLayoutManager = GridLayoutManager(requireContext(), 2)
                gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        if (position == 0) return 2
                        else return 1
                    }
                }
                rvCars.layoutManager = gridLayoutManager
            }
        }
    }

    fun onBtnClicked() {
        val dialog = BottomSheetFragment()
        dialog.show(childFragmentManager, BottomSheetFragment.BOTTOM_SHEET_FRAGMENT)
    }

    fun addCars(carsCount : Int) {
        val prevCarsCount : Int = cars!!.size
        for (step in 0 until carsCount) {
            val randomValue = Random.nextInt(0, cars!!.size)
            println("DEBUG TAG - $randomValue ${prevCarsCount}")
            cars!!.add(randomValue, CarsGenerator.generateCar())
        }
        cars!!.sortBy { car -> car.price }
        rvCarsAdapter?.setItems(cars!!)
    }
    private fun onItemStartClicked(position : Int) {
        rvCarsAdapter?.cars?.let {
            it[position].isFavorite = !it[position].isFavorite
        }
    }

    private fun onItemClicked(position: Int) {
        SimpleLocalStorage.car = rvCarsAdapter?.cars?.let {
            it[position - 1]
        }

        (requireActivity() as? BaseActivity)?.moveToScreen(
            ActionType.REPLACE,
            FragmentCarInfo.getInstance(position),
            FragmentCarInfo.FRAGMENT_CAR_INFO
        )
    }

    fun itemChanged(adapterPosition : Int) {
        rvCarsAdapter?.notifyItemChanged(adapterPosition)
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
        rvCarsAdapter = null
    }

    companion object {
        val CARS_FRAGMENT_TAG = "CARS_FRAGMENT_TAG"
        fun getInstance(carsCount : Int) : CarsFragment {
            val fragment : CarsFragment = CarsFragment()

            fragment.apply {
                arguments = bundleOf(Pair<String, Int>(ArgumentNames.CARS_COUNT, carsCount))
            }

            return fragment
        }
    }

}