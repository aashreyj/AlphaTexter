package com.example.alphatexter

import android.app.Application

class NumberLister : Application() {
    var numbers: ArrayList<Double> = arrayListOf() //list to be maintained

    fun getList(): ArrayList<Double>
    { //to retrieve the list
        return numbers
    }

    fun setList(list: ArrayList<Double>)
    { //to set the values in the list
        numbers = list
    }
}