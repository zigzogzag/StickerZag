package com.example.sticker

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.tab_layout_sticker_zag.view.*

class StickerZagTabLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr) {

    fun setupCustomView(
        adapter: PagerAdapter?,
        iconTab: MutableList<String>
    ) {
        var matrix = ColorMatrix()
        if (adapter != null) {

            for (position in 0 until adapter.count) {
                getTabAt(position)?.let {
                    val view = LayoutInflater.from(context)
                        .inflate(R.layout.tab_layout_sticker_zag, this, false)


                    val pathImg =
                        "file:///android_asset/stickertab/" + iconTab[position] + ".png"

                    Glide.with(this)
                        .load(pathImg)
                        .dontAnimate()
                        .into(view.iv_tab_icon)

                    if (position == 0) {
                        matrix.setSaturation(1f)
                        view.iv_tab_icon.colorFilter = ColorMatrixColorFilter(matrix)
                    } else {
                        matrix.setSaturation(0f)
                        view.iv_tab_icon.colorFilter = ColorMatrixColorFilter(matrix)
                    }

                    it.customView = view
                }
                addOnTabSelectedListener(object : OnTabSelectedListener {
                    override fun onTabReselected(tab: Tab) {
                    }

                    override fun onTabUnselected(tab: Tab) {

                    matrix.setSaturation(0f)
                    tab.customView?.iv_tab_icon!!.colorFilter = ColorMatrixColorFilter(matrix)
                    }

                    override fun onTabSelected(tab: Tab) {

                    matrix.setSaturation(1f)
                    tab.customView?.iv_tab_icon!!.colorFilter = ColorMatrixColorFilter(matrix)
                    }
                })
            }
        }
    }

}
