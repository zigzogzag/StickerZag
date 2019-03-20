package com.example.sticker.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.sticker.StickerZagGridView
import com.example.sticker.StickerZagRecentGridView

class StickerZagPagerAdapter (val views: MutableList<StickerZagGridView>) : PagerAdapter() {

    fun getRecentFragment(): StickerZagRecentGridView? {
        views.forEach {
            if (it is StickerZagRecentGridView) {
                return it
            }
        }
        return null
    }

    override fun isViewFromObject(view: View, key: Any): Boolean {
        return key == view
    }

    override fun getCount(): Int {
        return views.size
    }

    override fun instantiateItem(paramViewGroup: ViewGroup, paramInt: Int): Any {
        val v = views[paramInt].rootView
        (paramViewGroup as ViewPager).addView(v, 0)
        return v!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }
}
