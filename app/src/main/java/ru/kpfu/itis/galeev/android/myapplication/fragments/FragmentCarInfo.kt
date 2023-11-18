package ru.kpfu.itis.galeev.android.myapplication.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.base.BaseFragment
import ru.kpfu.itis.galeev.android.myapplication.databinding.FragmentCarInfoBinding
import ru.kpfu.itis.galeev.android.myapplication.model.Car
import ru.kpfu.itis.galeev.android.myapplication.utils.ArgumentNames
import ru.kpfu.itis.galeev.android.myapplication.utils.PriceConverter
import ru.kpfu.itis.galeev.android.myapplication.utils.SimpleLocalStorage

class FragmentCarInfo(
    private val onItemStarClicked : ((Int) -> Unit)? = null
) : BaseFragment(R.layout.fragment_car_info) {
    private var _viewBinding : FragmentCarInfoBinding? = null
    private val viewBinding get() = _viewBinding!!
    private var favoriteState : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(android.R.transition.slide_bottom)
        returnTransition = inflater.inflateTransition(android.R.transition.slide_bottom)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewBinding = FragmentCarInfoBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        initViews()
    }

    fun initViews() {
        val adapterPosition = arguments?.getInt(ArgumentNames.ADAPTER_POSITION)
        with (viewBinding) {
            with(SimpleLocalStorage.car!!) {
                tvCarName.setText(name)
                tvCarPrice.text = PriceConverter.convert(price)
                ivCarImg.setImageResource(img)
                ivFavoriteIc.setImageResource(
                    if (isFavorite) R.drawable.ic_favorite
                    else R.drawable.ic_favorite_not
                )
                tvDescription.setText(description)
                startPostponedEnterTransition()
            }


            ivFavoriteIc.setOnClickListener {
                SimpleLocalStorage.car!!.isFavorite = !SimpleLocalStorage.car!!.isFavorite
                requireActivity().supportFragmentManager.findFragmentByTag(CarsFragment.CARS_FRAGMENT_TAG).apply {
                    (this as? CarsFragment)?.apply {
                        itemChanged(adapterPosition!!)
                    }
                }
                ivFavoriteIc.setImageResource(
                    if (SimpleLocalStorage.car!!.isFavorite) R.drawable.ic_favorite
                    else R.drawable.ic_favorite_not
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
    companion object {
        val FRAGMENT_CAR_INFO = "FRAGMENT_CAR_INFO"
        fun getInstance(adapterPosition : Int) : FragmentCarInfo {
            val fragment : FragmentCarInfo = FragmentCarInfo()
            fragment.apply {
                arguments = bundleOf(
                    Pair(ArgumentNames.ADAPTER_POSITION, adapterPosition)
                )
            }
            return fragment
        }
    }
}