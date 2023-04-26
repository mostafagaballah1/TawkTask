package com.example.tawk.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tawk.R
/**
 *
 * Binding adapter that loads image using glide
 */
@BindingAdapter("loadImage")
fun loadImage(view: ImageView, photo: String?) {
    photo?.let {
        Glide.with(view.context)
            .load(it)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.drawable.ic_image_error)
            .placeholder(R.drawable.ic_image_background)
            .into(view)
    }
}

/**
 * sets refreshing status of swipe refresh layout
 */
@BindingAdapter("setRefreshStatus")
fun setRefreshStatus(
    view: SwipeRefreshLayout,
    status: Boolean
) {
    view.isRefreshing = status
}