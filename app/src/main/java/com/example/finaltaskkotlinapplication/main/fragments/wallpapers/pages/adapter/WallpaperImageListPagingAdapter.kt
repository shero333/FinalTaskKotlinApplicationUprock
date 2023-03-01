package com.example.finaltaskkotlinapplication.main.fragments.wallpapers.pages.adapter

import android.animation.Animator
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.finaltaskkotlinapplication.R
import com.example.finaltaskkotlinapplication.models.helperModels.UnsplashImageModelItem

class WallpaperImageListPagingAdapter(
    private val activity: Activity,
    private val view: FullView,
    private val imageSave: SaveImage,
    private val imagedelete: DeselectImage,
    private val snackbarView: SnackbarAnimation,
) :
    PagingDataAdapter<UnsplashImageModelItem, WallpaperImageListPagingAdapter.WallpaperImageListPagingViewHolder>(
        COMPARATOR
    ) {

    var itemNull: Boolean = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WallpaperImageListPagingViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.wallpaper_list_itemview, parent, false)
        return WallpaperImageListPagingViewHolder(view)
    }

    override fun onBindViewHolder(holder: WallpaperImageListPagingViewHolder, position: Int) {

        Log.d("ItemRecyclerView", "onBindViewHolder: ${getItem(position)}")

        val item = getItem(position)
        var i = 1

        try {
            if (item != null)
                holder.name.text = item.description.toString()
            else
                itemNull = true

        } catch (e: java.lang.Exception) {
            holder.name.text = "No description"
        }

        Glide.with(activity).load(item?.urls?.regular).into(holder.image)
        holder.fav.setAnimation(R.raw.add_to_favorite)


        holder.fav.setOnClickListener {

            if (item != null) {
                if (i == 1) {

                    if (holder.fav.visibility == View.GONE)
                        holder.fav.visibility = View.VISIBLE

                    holder.fav.setAnimation(R.raw.add_to_favorite)
                    holder.fav.playAnimation()

                    holder.fav.addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {
                            Log.e("Animation:", "start")
                            holder.fav.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            Log.e("Animation:", "end")

                            snackbarView.ShowSnackbar(i)

                        }

                        override fun onAnimationCancel(animation: Animator) {
                            Log.e("Animation:", "cancel")
                        }

                        override fun onAnimationRepeat(animation: Animator) {
                            Log.e("Animation:", "repeat")
                        }
                    })

                    imageSave.LikeItem(item)

                    i = 0
                } else {

                    holder.fav.setAnimation(R.raw.heart_break)
                    holder.fav.playAnimation()

                    holder.fav.addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {
                            Log.e("Animation:", "start")
                            holder.fav.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            Log.e("Animation:", "end")
//                        holder.fav.visibility = View.GONE
                            holder.fav.setAnimation(R.raw.add_to_favorite)

                            snackbarView.ShowSnackbar(i)

                        }

                        override fun onAnimationCancel(animation: Animator) {
                            Log.e("Animation:", "cancel")
                        }

                        override fun onAnimationRepeat(animation: Animator) {
                            Log.e("Animation:", "repeat")
                        }
                    })

//                imagedelete.DislikeItem(item)

                    i = 1
                }
            } else {
                itemNull = true
            }

        }

        holder.image.setOnClickListener {
            if (item != null) {
                view.ViewItem(item.urls.regular)
            } else
                itemNull = true
        }

//        if (itemNull)
//            Toast.makeText(activity,"Empty object",Toast.LENGTH_SHORT).show()

    }


    class WallpaperImageListPagingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: AppCompatImageView = itemView.findViewById(R.id.image_wallpaper)
        val name: AppCompatTextView = itemView.findViewById(R.id.wallpaper_name)
        val fav: LottieAnimationView = itemView.findViewById(R.id.fav_button)
    }

    companion object {

        private val COMPARATOR = object : DiffUtil.ItemCallback<UnsplashImageModelItem>() {

            override fun areItemsTheSame(
                oldItem: UnsplashImageModelItem,
                newItem: UnsplashImageModelItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UnsplashImageModelItem,
                newItem: UnsplashImageModelItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface FullView {

        fun ViewItem(regularImage: String)

    }

    interface SaveImage {

        fun LikeItem(regularImage: UnsplashImageModelItem)

    }

    interface DeselectImage {

        fun DislikeItem(regularImage: UnsplashImageModelItem)

    }

    interface SnackbarAnimation {

        fun ShowSnackbar(i: Int);
    }

}