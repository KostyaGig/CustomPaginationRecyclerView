package ru.zinoviewk.recyclerviewpet

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.zinoviewk.recyclerviewpet.data.CurrentDataState
import ru.zinoviewk.recyclerviewpet.data.PostsSource

abstract class ScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val postsSource: PostsSource,
    private val dataState: CurrentDataState
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val firstVisiblePositionItemCount = layoutManager.findFirstVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount

        val requestNewItems = visibleItemCount + firstVisiblePositionItemCount >= totalItemCount

        if (dataState.allowedToLoad()) {
            if (requestNewItems) {
                postsSource.fetchPosts()
            }
        }

    }
}