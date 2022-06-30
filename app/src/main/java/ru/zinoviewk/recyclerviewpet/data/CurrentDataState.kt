package ru.zinoviewk.recyclerviewpet.data

import android.util.Log
import androidx.lifecycle.Observer

interface CurrentDataState {

    fun allowedToLoad(): Boolean = false

    fun changeState(state: DataState) = Unit

    fun state() : DataState = DataState.Empty

    class Base(
        private val observer: Observer<DataState>
    ) : CurrentDataState {

        private var currentState: DataState = DataState.Empty

        override fun changeState(state: DataState) {
            Log.d("zinoviewks","CHANGE STATE $state")
            currentState = state
            observer.onChanged(state)
        }

        override fun allowedToLoad() = currentState.allowedToLoad()

        override fun state() = currentState
    }

    object Empty : CurrentDataState {
        override fun allowedToLoad() = true
    }

}