package ru.kpfu.itis.galeev.android.myapplication.utils

import ru.kpfu.itis.galeev.android.myapplication.R
import ru.kpfu.itis.galeev.android.myapplication.model.Car
import kotlin.random.Random

object CarsGenerator {
    val carsOptionsList : MutableList<Pair<Int, Int>> = createCarsOptionsList()

    fun generateCars(carsCount : Int) : MutableList<Car> {
        val list = mutableListOf<Car>()
        for(index in 0 until carsCount) {
            list.add(generateCar())
        }
        list.sortBy { car -> car.price }
        return list
    }

    private fun createCarsOptionsList() : MutableList<Pair<Int, Int>> {
        val _carsOptionsList : MutableList<Pair<Int, Int>> = mutableListOf()
        _carsOptionsList.add(Pair(R.string.gazel, R.drawable.gazel))
        _carsOptionsList.add(Pair(R.string.haval_jolion, R.drawable.haval_jolion))
        _carsOptionsList.add(Pair(R.string.lada_granta_2, R.drawable.lada_granta_2))
        _carsOptionsList.add(Pair(R.string.lada_granta_first, R.drawable.lada_granta_first))
        _carsOptionsList.add(Pair(R.string.lada_niva, R.drawable.lada_niva))
        _carsOptionsList.add(Pair(R.string.opel_astra_first, R.drawable.opel_astra_first))
        _carsOptionsList.add(Pair(R.string.opel_astra_second, R.drawable.opel_astra_second))
        _carsOptionsList.add(Pair(R.string.toyota_tank, R.drawable.toyota_tank))
        _carsOptionsList.add(Pair(R.string.vesta, R.drawable.vesta))
        return  _carsOptionsList
    }

    fun generateCar() : Car {
        val pair = carsOptionsList[Random.nextInt(0, carsOptionsList.size)]
        return Car(
            pair.first,
            pair.second,
            Random.nextInt(2, 15)*100_000
        )
    }
}