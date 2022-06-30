package ru.zinoviewk.recyclerviewpet.data

import androidx.annotation.CallSuper
import ru.zinoviewk.recyclerviewpet.Post
import ru.zinoviewk.recyclerviewpet.PostAdapter

interface DataState {

    fun allowedToLoad() : Boolean = false

    @CallSuper
    fun update(adapter: PostAdapter) = adapter.removeLast()


    object Progress : DataState {

        override fun update(adapter: PostAdapter) {
            super.update(adapter)
            adapter.update(Post.Progress)
        }

    }

    class Success(
        private val newPosts: List<Post>
    ) : DataState {
        override fun allowedToLoad() = true

        override fun update(adapter: PostAdapter) {
            super.update(adapter)
            adapter.addNewPosts(newPosts)
        }
    }

    class Failure(
        private val message: String
    ) : DataState {

        override fun update(adapter: PostAdapter) {
            super.update(adapter)
            adapter.update(Post.Failure(message))
        }
    }

    object Empty : DataState {
        override fun allowedToLoad() = true
    }
}