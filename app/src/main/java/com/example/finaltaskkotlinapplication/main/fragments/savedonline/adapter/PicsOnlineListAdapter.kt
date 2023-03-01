package com.example.finaltaskkotlinapplication.main.fragments.savedonline.adapter

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.finaltaskkotlinapplication.R
import com.example.finaltaskkotlinapplication.models.FirebaseImageModel
open class PicsOnlineListAdapter(
    private val activity: Activity,
    private val view: FullView,
    private val imageSave: SaveImage,
    private val snackbar: SnackbarAnimation,
    private val imageDeselect: DeselectImage
) :
    RecyclerView.Adapter<PicsOnlineListAdapter.OnlineSavedImageViewHolder>() {

    var imagesList: ArrayList<FirebaseImageModel>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnlineSavedImageViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.online_list_itemview, parent, false)
        return OnlineSavedImageViewHolder(view)
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: OnlineSavedImageViewHolder, position: Int) {

        var i = 1

        holder.name.text = imagesList!![position].name

        Log.i("imageUrl", ": ${imagesList!![position].imageUrl}")

//        Picasso.get().load(imagesList!![position].imageUrl).into(holder.image)

        holder.image.load(Uri.parse(imagesList!![position].imageUrl))

        holder.fav.setAnimation(R.raw.add_to_favorite)


        holder.fav.setOnClickListener {
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

                        snackbar.ShowSnackbar(i)

                    }

                    override fun onAnimationCancel(animation: Animator) {
                        Log.e("Animation:", "cancel")
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                        Log.e("Animation:", "repeat")
                    }
                })

                imageSave.LikeItem(imagesList!![position])

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

                        snackbar.ShowSnackbar(i)

                    }

                    override fun onAnimationCancel(animation: Animator) {
                        Log.e("Animation:", "cancel")
                    }

                    override fun onAnimationRepeat(animation: Animator) {
                        Log.e("Animation:", "repeat")
                    }
                })

                imageDeselect.DislikeItem(imagesList!![position])

                i = 1
            }
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

    class OnlineSavedImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: AppCompatImageView = itemView.findViewById(R.id.image_fb)
        val name: AppCompatTextView = itemView.findViewById(R.id.image_name_fb)
        val fav: LottieAnimationView = itemView.findViewById(R.id.fav_button_fb)
    }

    interface FullView {

        fun ViewItem(regularImage: FirebaseImageModel)

    }

    interface SaveImage {

        fun LikeItem(regularImage: FirebaseImageModel)

    }

    interface DeselectImage {

        fun DislikeItem(regularImage: FirebaseImageModel)

    }

    interface SnackbarAnimation {

        fun ShowSnackbar(i: Int);
    }
}