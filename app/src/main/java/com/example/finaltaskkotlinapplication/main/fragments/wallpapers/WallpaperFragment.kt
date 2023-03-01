package com.example.finaltaskkotlinapplication.main.fragments.wallpapers

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finaltaskkotlinapplication.R
import com.example.finaltaskkotlinapplication.databinding.FragmentWallpaperBinding
import com.example.finaltaskkotlinapplication.main.fragments.SharedDatabaseViewModel
import com.example.finaltaskkotlinapplication.main.fragments.wallpapers.pages.adapter.WallpaperImageListPagingAdapter
import com.example.finaltaskkotlinapplication.models.RoomImageModel
import com.example.finaltaskkotlinapplication.models.helperModels.UnsplashImageModelItem
import com.example.finaltaskkotlinapplication.utils.FullViewActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import layout.ListLoaderViewAdapter

@AndroidEntryPoint
class WallpaperFragment : Fragment(), WallpaperImageListPagingAdapter.FullView,
    WallpaperImageListPagingAdapter.SaveImage, WallpaperImageListPagingAdapter.DeselectImage,
    WallpaperImageListPagingAdapter.SnackbarAnimation {

    private lateinit var binding: FragmentWallpaperBinding

    private lateinit var viewModel: WallpaperViewModel
    private lateinit var SharedviewModel: SharedDatabaseViewModel
    private lateinit var listAdapter: WallpaperImageListPagingAdapter
    private var pageSize: Int = 50
    private var page: Int = 1
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var imagesFav: ArrayList<String> = ArrayList()
    private var i: Int = 0
    private lateinit var progressDialog: ProgressDialog

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[WallpaperViewModel::class.java]
        SharedviewModel = ViewModelProvider(this)[SharedDatabaseViewModel::class.java]

        binding = FragmentWallpaperBinding.inflate(layoutInflater, container, false)

        getData()

        binding.swipeRefreshUnsplash.setColorSchemeResources(R.color.icon_splash_background, R.color.icon_splash_background, R.color.icon_splash_background);

        binding.swipeRefreshUnsplash.setOnRefreshListener {

            getData()
            Handler().postDelayed({
                binding.swipeRefreshUnsplash.isRefreshing = false
            }, 4000)
        }

        progressDialog = ProgressDialog(requireContext(),R.style.AppCompatAlertDialogStyle)
        progressDialog.setTitle("Uploading image.....")
        progressDialog.show()

        listAdapter = WallpaperImageListPagingAdapter(requireActivity(), this, this, this, this)

        val manager = GridLayoutManager(context, 3)

        binding.listWallpapers.layoutManager = manager

        binding.listWallpapers.setHasFixedSize(true)

        binding.listWallpapers.scrollToPosition(listAdapter.itemCount - 1)

        binding.listWallpapers.adapter = listAdapter.withLoadStateHeaderAndFooter(
            header = ListLoaderViewAdapter(),
            footer = ListLoaderViewAdapter()
        )

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        viewModel.list.observe(viewLifecycleOwner) {

            Log.d("DataMain", "getData: $it")

            listAdapter.submitData(lifecycle, it)
            listAdapter.notifyDataSetChanged()

            progressDialog.dismiss()

        }
    }

    override fun ViewItem(regularImage: String) {

        val intent = Intent(requireActivity(), FullViewActivity::class.java)
        intent.putExtra("image", regularImage)
        startActivity(intent)

    }

    override fun LikeItem(regularImage: UnsplashImageModelItem) {

        try {

            SharedviewModel.LikeImage(
                regularImage.urls.regular,
                ((Long.MAX_VALUE - System.currentTimeMillis()).toInt()),
                regularImage.description.toString()
            )

        } catch (e: Exception) {

            SharedviewModel.LikeImage(
                regularImage.urls.regular,
                ((Long.MAX_VALUE - System.currentTimeMillis()).toInt()),
                "No name"
            )
        }

        Log.d("fav", "saveItem: $imagesFav")

    }

    override fun DislikeItem(regularImage: UnsplashImageModelItem) {

        if (regularImage.description.toString().isNotEmpty())
            SharedviewModel.dislikeImage(
                RoomImageModel(
                    regularImage.urls.regular,
                    regularImage.description.toString(),
                    ((Long.MAX_VALUE - System.currentTimeMillis()).toInt()))
            )
        else
            SharedviewModel.dislikeImage(
                RoomImageModel(
                    regularImage.urls.regular,
                    "No name",
                    ((Long.MAX_VALUE - System.currentTimeMillis()).toInt()))
            )


    }

    override fun ShowSnackbar(i: Int) {

        if (i == 1) {

            val snackbar = Snackbar.make(
                requireView(), "Removed from favourites!",
                Snackbar.LENGTH_LONG
            ).setAction("Ok", null)
            snackbar.setActionTextColor(Color.BLUE)
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(resources.getColor(R.color.icon_splash_background))
            val textView =
                snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.WHITE)
            textView.textSize = 28f
            snackbar.show()

        } else {
            val snackbar = Snackbar.make(
                requireView(), "Added to favourites!",
                Snackbar.LENGTH_LONG
            ).setAction("Ok", null)
            snackbar.setActionTextColor(Color.BLUE)
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(resources.getColor(R.color.icon_splash_background))

            val textView =
                snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView

            textView.setTextColor(Color.WHITE)
            textView.textSize = 28f
            snackbar.show()
        }


    }


}