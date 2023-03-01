package com.example.finaltaskkotlinapplication.main.fragments.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.finaltaskkotlinapplication.main.fragments.favorites.FavoriteWPFragment
import com.example.finaltaskkotlinapplication.main.fragments.savedonline.PicsSavedOnlineFragment
import com.example.finaltaskkotlinapplication.main.fragments.wallpapers.WallpaperFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        if (position == 0) {
            fragment = WallpaperFragment()
        } else if (position == 1) {
            fragment = PicsSavedOnlineFragment()
        } else if (position == 2) {
            fragment = FavoriteWPFragment()
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) {
            title = "Wallpapers"
        } else if (position == 1) {
            title = "Saved ones"
        } else if (position == 2) {
            title = "Favorites"
        }
        return title
    }
}