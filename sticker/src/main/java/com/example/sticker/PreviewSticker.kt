package com.example.sticker

import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.example.sticker.model.StickerZagItemData

class PreviewSticker(
    private val context: Context,
    private val rootView: ViewGroup
) : PopupWindow(context) {

    fun setPreview(group: String, toy: StickerZagItemData) {
        LayoutInflater.from(context).inflate(R.layout.layout_preview_sticker, rootView, false).apply {
            val imagePath = "file:///android_asset/$group/${toy.image}.${toy.imageExtension}"
//            Timber.d("setPreview() called with: group = [$group], pathImg = [$imagePath]")
            Glide.with(context)
                .load(Uri.parse(imagePath))
                .into(findViewById(R.id.iv_preview_sticker))
        }.run {
            contentView = this
            setSize(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
            showAtBottom()
        }
    }

    private fun setSize(width: Int, height: Int) {
        setWidth(width)
        setHeight(height)
    }

    private fun showAtBottom() {
        showAtLocation(rootView, Gravity.BOTTOM, 0, 0)
    }
}
