package com.example.sticker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.sticker.R
import com.example.sticker.model.StickerZagItemData

typealias OnStickerZagItemClick = (StickerZagItemData, String) -> Unit
typealias OnStickerZagItemLongClick = (StickerZagItemData, String) -> Unit
typealias OnStickerZagItemTouchClick = (StickerZagItemData, MotionEvent) -> Unit

class StickerZagAdapter (
    val context: Context,
    var data: List<StickerZagItemData>,
    var group: String?
) : BaseAdapter() {

    var onStickerZagItemClick: (OnStickerZagItemClick)? = null
    var onStickerZagItemLongClick: (OnStickerZagItemLongClick)? = null
    var onStickerZagItemTouchClick: (OnStickerZagItemTouchClick)? = null

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return data.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v = convertView
        if (v == null) {
            v = View.inflate(context, R.layout.sticker_zag_item, null)
            val holder = ViewHolder()
            holder.image = v!!.findViewById<View>(R.id.iv_sticker_zag) as ImageView
            v.tag = holder
        }
        val item = data[position]
        val holder = v.tag as ViewHolder

        group = if (item.group != null) item.group!! else group

        val imagePath = "file:///android_asset/$group/${item.image}.${item.imageExtension}"

        Glide.with(context)
            .load(Uri.parse(imagePath))
            .error(R.drawable.ic_launcher_background)
            .into(holder.image!!)

        holder.image!!.setOnClickListener {
            onStickerZagItemClick?.invoke(item, this!!.group!!)
        }
        holder.image!!.setOnLongClickListener {
            onStickerZagItemLongClick?.invoke(item, this!!.group!!)
            true
        }
        holder.image!!.setOnTouchListener { v, event ->
            onStickerZagItemTouchClick?.invoke(item, event)
            false
        }
        return v
    }

    internal inner class ViewHolder {
        var image: ImageView? = null
    }
}
