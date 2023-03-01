package com.example.finaltaskkotlinapplication.main.fragments.favorites.adapter

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.finaltaskkotlinapplication.R
import com.example.finaltaskkotlinapplication.models.RoomImageModel

open class FavListAdapter(
    private val activity: Activity,
    private val view: FullView,
    private val imagedeselect: DeselectImage,
    private val snackbar: SnackbarAnimation
) :
    RecyclerView.Adapter<FavListAdapter.FavImageViewHolder>() {

    var imagesList: List<RoomImageModel>? = null
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavImageViewHolder {

        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.fav_list_itemview, parent, false)
        return FavImageViewHolder(view)
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: FavImageViewHolder, position: Int) {

        val i = 0
        try {
            holder.name.text = imagesList!![position].name

        } catch (e: java.lang.Exception) {
            holder.name.text = "No description"
        }

        Glide.with(activity).load(imagesList!![position].url).into(holder.image)

        holder.fav.setAnimation(R.raw.add_to_favorite)

        holder.fav.setOnClickListener {

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

                    imagedeselect.DislikeItemRoom(imagesList!![position])

                    snackbar.ShowSnackbar(i)

                }

                override fun onAnimationCancel(animation: Animator) {
                    Log.e("Animation:", "cancel")
                }

                override fun onAnimationRepeat(animation: Animator) {
                    Log.e("Animation:", "repeat")
                }
            })


        }

        holder.image.setOnClickListener {
            view.ViewItem(imagesList!![position])
        }

    }

    override fun getItemCount(): Int {

        if (imagesList != null)
            return imagesList!!.size
        else
            return 0
    }

    class FavImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: AppCompatImageView = itemView.findViewById(R.id.image_fv)
        val name: AppCompatTextView = itemView.findViewById(R.id.image_name_fv)
        val fav: LottieAnimationView = itemView.findViewById(R.id.fav_button_fv)
    }

    interface FullView {
        fun ViewItem(regularImage: RoomImageModel)

    }

    interface DeselectImage {
        fun DislikeItemRoom(regularImage: RoomImageModel)

    }

    interface SnackbarAnimation {

        fun ShowSnackbar(i: Int);
    }
}