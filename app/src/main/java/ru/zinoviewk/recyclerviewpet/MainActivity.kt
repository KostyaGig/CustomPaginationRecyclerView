package ru.zinoviewk.recyclerviewpet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.zinoviewk.recyclerviewpet.data.CurrentDataState
import ru.zinoviewk.recyclerviewpet.data.PostsSource

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = PostAdapter.Base()

        val currentDataState = CurrentDataState.Base { state ->
            lifecycleScope.launch {
                state.update(adapter)
            }
        }

        val scope = CoroutineScope(Dispatchers.IO)
        val source = PostsSource.Base(
            currentDataState,
            scope
        )

        adapter.addRetryListener(source)

        val layoutManager = LinearLayoutManager(this@MainActivity)
        val scrollListener = object : ScrollListener(layoutManager,source,currentDataState) {}

        with(findViewById<RecyclerView>(R.id.recycler_view)) {
            this.adapter = adapter
            this.addItemDecoration(
                DividerItemDecoration(
                    this.context, DividerItemDecoration.VERTICAL
                )
            )
            this.layoutManager = layoutManager
            this.addOnScrollListener(scrollListener)
        }

        adapter.update(source.firstPosts())

    }

}