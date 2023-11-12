package ru.kpfu.itis.galeev.android.myapplication.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.galeev.android.myapplication.model.Car

class CarsDiffUtil(
    private val oldItemList : List<Car>,
    private val newItemList : List<Car>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItemList.size
    }

    override fun getNewListSize(): Int {
        return newItemList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemList[oldItemPosition]
        val newItem = newItemList[newItemPosition]

        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        /* there is no database in my application and therefore there is no sense in double checking
           for the identity of the item and the content
        */
        return areItemsTheSame(oldItemPosition, newItemPosition)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}