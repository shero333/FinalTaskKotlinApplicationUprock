package com.example.finaltaskkotlinapplication.main.fragments.favorites

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finaltaskkotlinapplication.R
import com.example.finaltaskkotlinapplication.databinding.FragmentFavoriteWpBinding
import com.example.finaltaskkotlinapplication.main.fragments.SharedDatabaseViewModel
import com.example.finaltaskkotlinapplication.main.fragments.favorites.adapter.FavListAdapter
import com.example.finaltaskkotlinapplication.utils.FullViewActivity
import com.example.finaltaskkotlinapplication.models.RoomImageModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteWPFragment : Fragment(), FavListAdapter.FullView, FavListAdapter.DeselectImage,
    FavListAdapter.SnackbarAnimation {

    private lateinit var viewModel: FavoriteWPViewModel
    private lateinit var SharedviewModel: SharedDatabaseViewModel
    private lateinit var binding:FragmentFavoriteWpBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var listAdapter:FavListAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteWpBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FavoriteWPViewModel::class.java]
        SharedviewModel = ViewModelProvider(this)[SharedDatabaseViewModel::class.java]

        listAdapter = FavListAdapter(requireActivity(),this,this,this)

        val manager = GridLayoutManager(context, 3)

        binding.favImageList.layoutManager = manager

        binding.favImageList.setHasFixedSize(true)

        progressDialog = ProgressDialog(requireContext(),R.style.AppCompatAlertDialogStyle)
        progressDialog.setMessage("Loading....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        viewModel.getFavs()
        viewModel.getFavsList().observe(viewLifecycleOwner) {

            if (it.isNotEmpty()){

                Log.d("favs", "onCreateView: $it")

                listAdapter.imagesList = it

                binding.favImageList.adapter = listAdapter

                listAdapter.notifyDataSetChanged()
                progressDialog.dismiss()
            }else{
                progressDialog.dismiss()

                binding.favImageList.visibility = View.GONE
                binding.noList.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun ViewItem(regularImage: RoomImageModel) {
        val intent = Intent(requireActivity(), FullViewActivity::class.java)
        intent.putExtra("image",regularImage.url)
        startActivity(intent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun DislikeItemRoom(regularImage: RoomImageModel) {

        SharedviewModel.dislikeImage(regularImage)

        viewModel.getFavs()
        viewModel.getFavsList().observe(viewLifecycleOwner) {

            if (it.isNotEmpty()){

                Log.d("favs", "onCreateView: $it")

                listAdapter.imagesList = it

                binding.favImageList.adapter = listAdapter

                listAdapter.notifyDataSetChanged()
                progressDialog.dismiss()
            }else{
                progressDialog.dismiss()

                binding.favImageList.visibility = View.GONE
                binding.noList.visibility = View.VISIBLE
            }

        }

    }

    override fun ShowSnackbar(i: Int) {
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
    }

}