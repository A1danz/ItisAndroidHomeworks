package ru.kpfu.itis.galeev.android.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.databinding.CarItemBigBinding
import ru.kpfu.itis.galeev.android.myapplication.databinding.CarItemSmallBinding
import ru.kpfu.itis.galeev.android.myapplication.databinding.DividerContentItemBinding
import ru.kpfu.itis.galeev.android.myapplication.databinding.HeaderItemBtnBinding
import ru.kpfu.itis.galeev.android.myapplication.diffutil.CarsDiffUtil
import ru.kpfu.itis.galeev.android.myapplication.model.Car
import ru.kpfu.itis.galeev.android.myapplication.utils.PriceConverter

class RVCarsAdapter(
    private val context: Context,
    private val onItemStarClicked : ((Int) -> Unit)? = null,
    private val onItemClicked: ((Int, CardView) -> Unit)? = null,
    private val onBtnClicked: (() -> Unit)? = null,
    private val onItemDeleted: ((Int) -> Unit)? = null
    ) :
    RecyclerView.Adapter<ViewHolder>() {

    val cars = mutableListOf<Car>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            BIG_CAR_VIEW_TYPE -> {
                CarBigViewHolder(
                    viewBinding = CarItemBigBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            SMALL_CAR_VIEW_TYPE -> {
                CarSmallViewHolder(
                    viewBinding = CarItemSmallBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            DIVIDER_VIEW_TYPE -> {
                DividerConentHolder(
                    viewBinding = DividerContentItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                HeaderBtnViewHolder(
                    viewBinding = HeaderItemBtnBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return cars.size + 1 + cars.size / 8
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is CarSmallViewHolder ) {
            holder.bindItem(cars[position - 1 - position / 8])
        } else if (holder is CarBigViewHolder) {
            holder.bindItem(cars[position - 1 - position / 8])
        } else if (holder is DividerConentHolder) {
            if (position != this.itemCount - 1) {
                holder.bindItem(cars[(position + 1) - 1 - position / 8].price)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) { return HEADER_BTN_VIEW_TYPE }
        else {
            if (position % 8 == (position / 8) % 8) return DIVIDER_VIEW_TYPE
            if (cars.size > 12) {
                return SMALL_CAR_VIEW_TYPE
            } else {
                return BIG_CAR_VIEW_TYPE
            }
        }
    }

    fun setItems(newList : List<Car>) {
        val diff = CarsDiffUtil(cars, newList)
        val diffResult = DiffUtil.calculateDiff(diff)

        cars.clear()
        cars.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class CarBigViewHolder(private val viewBinding: CarItemBigBinding) :
        ViewHolder(viewBinding.root) {
        fun bindItem(car: Car) {
            with(viewBinding) {
                tvCarName.text = context.resources.getString(car.name)
                tvCarPrice.text = PriceConverter.convert(car.price)
                ivCarImg.setImageResource(car.img)
                ivFavoriteIc.setImageResource(
                    if (car.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_not
                )
            }
        }

        init {
            with(viewBinding) {
                ivFavoriteIc.setOnClickListener {
                    onItemStarClicked?.invoke(adapterPosition - 1 - adapterPosition / 8)
                    notifyItemChanged(adapterPosition)
                }

                cvCar.setOnClickListener {
                    onItemClicked?.invoke(adapterPosition, cvCar)
                }
            }
        }
    }

    inner class CarSmallViewHolder(private val viewBinding: CarItemSmallBinding) :
        ViewHolder(viewBinding.root) {
        fun bindItem(car: Car) {
            with(viewBinding) {
                tvCarName.text = context.resources.getString(car.name)
                tvCarPrice.text = PriceConverter.convert(car.price)
                ivCarImg.setImageResource(car.img)
                ivFavoriteIc.setImageResource(
                    if (car.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_not
                )
            }
        }

        init {
            with(viewBinding) {
                ivFavoriteIc.setOnClickListener {
                    onItemStarClicked?.invoke(adapterPosition - 1 - adapterPosition / 8)
                    notifyItemChanged(adapterPosition)
                }

                cvCar.setOnClickListener {
                    if (overlayRect.visibility == View.GONE) {
                        onItemClicked?.invoke(adapterPosition, cvCar)
                    } else {
                        onItemDeleted?.invoke(adapterPosition)
                    }
                }

                cvCar.setOnLongClickListener(object : OnLongClickListener {
                    override fun onLongClick(v: View?): Boolean {
                        if (overlayRect.visibility == View.GONE) {
                            overlayRect.visibility = View.VISIBLE
                            println("DEBUG TAG - VISIBLE")
                        } else {
                            overlayRect.visibility = View.GONE
                            println("DEBUG TAG - INVISIBLE")
                        }
                        return true
                    }
                })
            }
        }
    }



    inner class HeaderBtnViewHolder(private val viewBinding: HeaderItemBtnBinding) :
        ViewHolder(viewBinding.root) {
            init {
                with (viewBinding) {
                    btnAddCar.setOnClickListener {
                        onBtnClicked?.invoke()
                    }
                }
            }
        }

    inner class DividerConentHolder(private val viewBinding: DividerContentItemBinding):
        ViewHolder(viewBinding.root) {
        fun bindItem(price : Int) {
            with (viewBinding) {
                tvPriceDivider.text = ">= ${PriceConverter.convert(price)}"
                tvPriceDivider.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        val HEADER_BTN_VIEW_TYPE = 1
        val BIG_CAR_VIEW_TYPE = 2
        val SMALL_CAR_VIEW_TYPE = 3
        val DIVIDER_VIEW_TYPE = 4
    }

}