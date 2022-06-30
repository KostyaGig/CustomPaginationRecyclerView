package ru.zinoviewk.recyclerviewpet

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

interface Post {

    fun viewType() : Int

    fun bind(title: TextView,content: TextView) = Unit
    fun bind(image: ImageView) = Unit
    fun bind(error: TextView) = Unit

    object Progress : Post {
        override fun viewType() = 1
    }

    class Failure(
        private val message: String
    ) : Post {

        override fun bind(error: TextView) {
            error.text = message
        }

        override fun viewType() = 2
    }

    class Text(
        private val title: String,
        private val content: String
    ) : Post  {

        override fun bind(title: TextView, content: TextView) {
            title.text = this.title
            content.text = this.content
        }

        override fun viewType() = 3
    }

    class Image(
        private val image: String
    ) : Post {

        override fun bind(image: ImageView) {
            Glide
                .with(image.context)
                .load(this.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image)
        }

        override fun viewType() = 4
    }

    class TextWithImage(
        private val text: Text,
        private val image: Image
    ) : Post {

        override fun bind(image: ImageView) = this.image.bind(image)
        override fun bind(title: TextView, content: TextView) = text.bind(title, content)

        override fun viewType() = 5
    }

    class Advertisement(
        private val textWithImage: TextWithImage
    ) : Post {

        override fun bind(image: ImageView) = textWithImage.bind(image)
        override fun bind(title: TextView, content: TextView) = textWithImage.bind(title, content)

        override fun viewType() = 6
    }

}