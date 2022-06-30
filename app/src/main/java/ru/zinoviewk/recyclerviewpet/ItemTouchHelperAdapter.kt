package ru.zinoviewk.recyclerviewpet

interface ItemTouchHelperAdapter {

    fun move(fromPosition: Int,toPosition: Int)
    fun remove(position: Int)
}