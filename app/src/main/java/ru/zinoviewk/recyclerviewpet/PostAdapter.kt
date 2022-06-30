package ru.zinoviewk.recyclerviewpet

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

interface PostAdapter : ItemTouchHelperAdapter {

    fun update(posts: List<Post>)

    fun update(post: Post)

    fun addNewPosts(posts: List<Post>)

    fun removeLast()

    fun addRetryListener(listener: RetryListener)
    class Base : RecyclerView.Adapter<Base.ViewHolder>(), PostAdapter {

        private val posts = mutableListOf<Post>()

        private var retryListener: RetryListener = RetryListener.Empty
        override fun addRetryListener(listener: RetryListener) { retryListener = listener }

        override fun update(posts: List<Post>) {
            this.posts.clear()
            this.posts.addAll(posts)
            notifyDataSetChanged()
        }

        override fun addNewPosts(posts: List<Post>) {
            this.posts.addAll(posts)
            notifyDataSetChanged()
        }

        override fun update(post: Post) {
            this.posts.add(post)
            notifyItemInserted(this.posts.size - 1)
        }

        override fun removeLast() {
            val lastIndex = this.posts.lastIndex
            this.posts.removeLast()
            notifyItemRemoved(lastIndex)
        }

        override fun getItemViewType(position: Int) = posts[position].viewType()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return when (viewType) {
                1 -> ViewHolder.Progress(inflater.inflate(R.layout.post_progress, parent, false))
                2 -> ViewHolder.Failure(
                    inflater.inflate(R.layout.post_failure, parent, false),
                    retryListener
                )
                3 -> ViewHolder.Text(inflater.inflate(R.layout.post_text, parent, false))
                4 -> ViewHolder.Image(inflater.inflate(R.layout.post_image, parent, false))
                5 -> ViewHolder.TextWithImage(
                    inflater.inflate(
                        R.layout.post_text_with_image,
                        parent,
                        false
                    )
                )
                6 -> ViewHolder.Advertisement(
                    inflater.inflate(
                        R.layout.post_advertisement,
                        parent,
                        false
                    )
                )
                else -> throw IllegalArgumentException("onCreateViewHolder doesn't handle view type $viewType")
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(posts[position])

        override fun getItemCount() = posts.size

        override fun move(fromPosition: Int, toPosition: Int) {
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(posts, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(posts, i, i - 1)
                }
            }
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun remove(position: Int) {
            Log.d("zinoviewks", "remove by $position")
            posts.removeAt(position)
            notifyItemRemoved(position)
        }

        abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            abstract fun bind(post: Post)

            class Progress(view: View) : ViewHolder(view) {
                override fun bind(post: Post) = Unit
            }

            class Failure(
                private val view: View,
                private val retryListener: RetryListener
            ) : ViewHolder(view) {
                override fun bind(post: Post) {
                    post.bind(view.findViewById<TextView>(R.id.error_text_view))

                    view.findViewById<Button>(R.id.retry_btn).setOnClickListener { retryListener.onRetry() }
                }
            }

            class Text(private val view: View) : ViewHolder(view) {


                override fun bind(post: Post) {
                    post.bind(
                        view.findViewById(R.id.post_text_view),
                        view.findViewById(R.id.post_title_view)
                    )
                }

            }

            class Image(private val view: View) : ViewHolder(view) {

                override fun bind(post: Post) {
                    post.bind(view.findViewById<ImageView>(R.id.post_image_view))
                }

            }

            class TextWithImage(private val view: View) : ViewHolder(view) {

                override fun bind(post: Post) {
                    with(post) {
                        bind(
                            view.findViewById(R.id.post_text_view),
                            view.findViewById(R.id.post_title_view)
                        )
                        bind(view.findViewById<ImageView>(R.id.post_image_view))
                    }
                }
            }

            class Advertisement(private val view: View) : ViewHolder(view) {

                override fun bind(post: Post) {
                    with(post) {
                        bind(
                            view.findViewById(R.id.post_text_view),
                            view.findViewById(R.id.post_title_view)
                        )
                        bind(view.findViewById<ImageView>(R.id.post_image_view))
                    }
                }
            }
        }
    }
}