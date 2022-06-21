package com.example.project2020.user_utilities

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.project2020.R

fun popImage(context: Context?, uri:String?, imageView: ImageView, errorDrawable: Int = R.drawable.empty){
    if (context != null) {
    val options = RequestOptions()
        .placeholder(progressDrawable(context))
        .error(errorDrawable)
        Glide.with(context)
            .load(uri)
            .apply(options)
            .into(imageView)
    }
}
fun progressDrawable(context: Context): CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth =5f
        centerRadius = 30f
        start()
    }
}