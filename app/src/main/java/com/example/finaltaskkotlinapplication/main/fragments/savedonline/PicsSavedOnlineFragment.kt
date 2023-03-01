package com.example.finaltaskkotlinapplication.main.fragments.savedonline

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finaltaskkotlinapplication.R
import com.example.finaltaskkotlinapplication.databinding.FragmentPicsSavedOnlineBinding
import com.example.finaltaskkotlinapplication.main.fragments.SharedDatabaseViewModel
import com.example.finaltaskkotlinapplication.main.fragments.savedonline.adapter.PicsOnlineListAdapter
import com.example.finaltaskkotlinapplication.models.FirebaseImageModel
import com.example.finaltaskkotlinapplication.models.RoomImageModel
import com.example.finaltaskkotlinapplication.utils.FullViewActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PicsSavedOnlineFragment : Fragment(), PicsOnlineListAdapter.SaveImage,
    PicsOnlineListAdapter.FullView, PicsOnlineListAdapter.DeselectImage,
    PicsOnlineListAdapter.SnackbarAnimation {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var viewModel: PicsSavedOnlineViewModel
    private var binding: FragmentPicsSavedOnlineBinding? = null
    private lateinit var listAdapter: PicsOnlineListAdapter
    private lateinit var SharedviewModel: SharedDatabaseViewModel
    private var i: Int = 0

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentPicsSavedOnlineBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(this)[PicsSavedOnlineViewModel::class.java]
        SharedviewModel = ViewModelProvider(this)[SharedDatabaseViewModel::class.java]


        listAdapter = PicsOnlineListAdapter(requireActivity(), this, this, this, this)

        val manager = GridLayoutManager(context, 3)

        binding!!.onlineImageList.layoutManager = manager

        binding!!.onlineImageList.setHasFixedSize(true)

        binding!!.onlineImageList.adapter = listAdapter


        progressDialog = ProgressDialog(requireContext(),R.style.AppCompatAlertDialogStyle)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        viewModel.getFirebaseImages()

        viewModel.getImages().observe(viewLifecycleOwner) {

            if (it.isNotEmpty()) {

                Log.d("FBData", "onCreateView: ${it[0].imageUrl}")

                listAdapter.imagesList = it
                listAdapter.notifyDataSetChanged()
                progressDialog.dismiss()

            } else {
                progressDialog.dismiss()
                binding!!.onlineImageList.visibility = View.GONE
                binding!!.noList.visibility = View.VISIBLE
            }
        }

        binding!!.swipeRefreshFb.setColorSchemeResources(R.color.icon_splash_background, R.color.icon_splash_background, R.color.icon_splash_background);


        binding!!.swipeRefreshFb.setOnRefreshListener {

            viewModel.getFirebaseImages()

            viewModel.getImages().observe(viewLifecycleOwner) {

                if (it.isNotEmpty()) {

                    Log.d("FBData", "onCreateView: ${it[0].imageUrl}")

                    listAdapter.imagesList = it
                    listAdapter.notifyDataSetChanged()
                    progressDialog.dismiss()

                } else {
                    binding!!.onlineImageList.visibility = View.GONE
                    binding!!.noList.visibility = View.VISIBLE
                }
            }

            Handler().postDelayed({
                binding!!.swipeRefreshFb.isRefreshing = false
            }, 4000)
        }

        return binding!!.root
    }


    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onActivityCreated(savedInstanceState)",
            "androidx.fragment.app.Fragment"
        )
    )
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun ViewItem(regularImage: FirebaseImageModel) {
        val intent = Intent(requireActivity(), FullViewActivity::class.java)
        intent.putExtra("image", regularImage.imageUrl)
        startActivity(intent)
    }

    override fun DislikeItem(regularImage: FirebaseImageModel) {

        SharedviewModel.dislikeImage(
            RoomImageModel(
                regularImage.imageUrl!!,
                "No name",
                ((Long.MAX_VALUE - System.currentTimeMillis()).toInt())
            )
        )

    }

    override fun LikeItem(regularImage: FirebaseImageModel) {

        SharedviewModel.LikeImage(
            regularImage.imageUrl.toString(),
            ((Long.MAX_VALUE - System.currentTimeMillis()).toInt()),
            "No name")

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