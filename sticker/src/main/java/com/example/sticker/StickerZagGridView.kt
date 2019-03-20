package com.example.sticker

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.GridView
import com.example.sticker.adapter.StickerZagAdapter
import com.example.sticker.model.StickerZagData
import com.example.sticker.model.StickerZagItemData

open class StickerZagGridView (
    val context: Context,
    var data: StickerZagData?,
    val recents: StickerZagRecent
) {
    var rootView: View? = null

    val listener: OnStickerZagClickedListener? = (context as? OnStickerZagClickedListener)

    lateinit var stickerAdapter: StickerZagAdapter

    init {
        data?.let { data ->
            rootView = (context.getSystemService(
                Activity.LAYOUT_INFLATER_SERVICE
            ) as? LayoutInflater)?.run {
                inflate(R.layout.layout_sticker_zag_grid_view, null)?.apply {
                    (findViewById<GridView>(R.id.grid_view)).apply {
                        stickerAdapter = StickerZagAdapter(context, data.items!!, data.group!!)
                        stickerAdapter.onStickerZagItemClick = { item, group ->
                            listener?.onStickerZagClicked(item)
                            recents.addRecent(item, group)
                        }
                        stickerAdapter.onStickerZagItemLongClick = { item, group ->
                            listener?.onStickerZagLongClicked(item, group)
                        }
                        stickerAdapter.onStickerZagItemTouchClick = { item, event ->
                            listener?.onStickerZagTouchClicked(item, event)

                        }
                        adapter = stickerAdapter
                    }
                }
            }
        }
    }

    interface OnStickerZagClickedListener {
        fun onStickerZagClicked(item: StickerZagItemData)
        fun onStickerZagLongClicked(item: StickerZagItemData, group: String)
        fun onStickerZagTouchClicked(item: StickerZagItemData, event: MotionEvent)
    }
}
