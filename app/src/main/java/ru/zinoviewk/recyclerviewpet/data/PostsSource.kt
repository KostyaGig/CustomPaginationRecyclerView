package ru.zinoviewk.recyclerviewpet.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.zinoviewk.recyclerviewpet.Post
import ru.zinoviewk.recyclerviewpet.RetryListener

interface PostsSource {

    fun firstPosts() : List<Post>

    fun fetchPosts()

    class Base(
        private val state: CurrentDataState,
        private val loadScope: CoroutineScope,
    ) : PostsSource, RetryListener {

        private var pageOfSources = START_PAGE

        override fun firstPosts(): List<Post> {
            return listOf(
                Post.Text("First title","Content for the first title"),
                Post.Text("Second title","Content for the second title"),
                Post.TextWithImage(Post.Text(
                    "Title for image","content for image"
                ),Post.Image(IMAGE_URL)),
                Post.Text("First title","Content for the first title"),
                Post.Advertisement(
                    Post.TextWithImage(
                        Post.Text("The first advertisement","Text for the first advertisement")
                        ,Post.Image(IMAGE_URL))
                ),
                Post.Image(IMAGE_URL),
                Post.Advertisement(
                    Post.TextWithImage(
                        Post.Text("the second advertisement","Text for the second advertisement")
                        ,Post.Image(IMAGE_URL))
                )
            )
        }

        override fun fetchPosts() {
            loadScope.launch {
                pageOfSources++
                state.changeState(DataState.Progress)
                delay(3000)
                val posts = mutableListOf<Post>()
                for (index in 0..10) {
                    posts.add(
                        Post.Text("Post with number $index", "Page of the post is $pageOfSources")
                    )
                }

                if (pageOfSources % 3 == 0) {
                    state.changeState(
                        DataState.Failure(
                            "Post with page $pageOfSources can not be loaded"
                        )
                    )
                } else {
                    state.changeState(DataState.Success(posts))
                }

            }
        }

        private companion object {
            private const val START_PAGE = 0

            private const val IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/exoplayer-46fe1.appspot.com/o/bandana_wrap.jpeg?alt=media&token=5c35ae1a-769c-40bd-b20e-228f0419a036"
        }

        override fun onRetry() { fetchPosts() }
    }
}