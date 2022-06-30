package ru.zinoviewk.recyclerviewpet

interface RetryListener {

    fun onRetry()

    object Empty : RetryListener {
        override fun onRetry() = Unit
    }
}