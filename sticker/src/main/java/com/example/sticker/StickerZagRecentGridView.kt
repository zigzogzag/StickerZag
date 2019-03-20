package com.example.sticker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.GridView
import android.widget.TextView
import com.example.sticker.adapter.StickerZagAdapter
import com.example.sticker.db.StickerZagDb
import com.example.sticker.model.StickerZagItemData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StickerZagRecentGridView (
    context: Context,
    recents: StickerZagRecent
) : StickerZagGridView(context, null, recents), StickerZagRecent {

    private val db by lazy {
        StickerZagDb.create(context)
    }

    init {
        rootView = (context.getSystemService(
            Activity.LAYOUT_INFLATER_SERVICE
        ) as? LayoutInflater)?.run {
            inflate(R.layout.layout_sticker_zag_grid_view, null)?.apply {
                (findViewById<GridView>(R.id.grid_view)).apply {
                    stickerAdapter = StickerZagAdapter(context, emptyList(), "")
                    stickerAdapter.onStickerZagItemClick = { item, group ->
                        listener?.onStickerZagClicked(item)
                        addRecent(item)
                    }
                    stickerAdapter.onStickerZagItemLongClick = { item, group ->
                        listener?.onStickerZagLongClicked(item, item.group!!)
                    }
                    stickerAdapter.onStickerZagItemTouchClick = { item, event ->
                        listener?.onStickerZagTouchClicked(item, event)

                    }
                    adapter = stickerAdapter
                }
            }
        }


        loadRecentList()
    }

    @SuppressLint("CheckResult")
    private fun loadRecentList() {
        Observable.fromCallable { db.stickerZagDb().getStickerZagItemOrderBy() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items ->
                if (items.isNotEmpty()) {
                    stickerAdapter.data = items
                    stickerAdapter.notifyDataSetChanged()
                    hideTextEmpty()
                } else{
                    showTextEmpty()
                }
                Log.d("loadRecentList: items ", items.toString())
            }, { error ->
                Log.d("loadRecentList: error ", error.message)
            })
    }

    private fun hideTextEmpty() {
        val textView = rootView!!.findViewById<TextView>(R.id.tv_empty_sticker)
        textView.visibility = View.GONE

    }

    private fun showTextEmpty() {
        val textView = rootView!!.findViewById<TextView>(R.id.tv_empty_sticker)
        textView.visibility = View.VISIBLE

    }

    @SuppressLint("CheckResult")
    private fun addRecent(item: StickerZagItemData) {
        Observable.fromCallable {
            db.stickerZagDb().insertStickerZag(
                item.copy(
                    createDate = System.currentTimeMillis()
                )
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items ->
                Log.d("addRecent : items = ", items.toString())
            }, { error ->
                Log.d("addRecent : error = ", error.message)
            })
    }

    @SuppressLint("CheckResult")
    override fun addRecent(item: StickerZagItemData, group: String?) {
        Observable.fromCallable {
            db.stickerZagDb().insertStickerZag(
                item.copy(
                    group = group,
                    createDate = System.currentTimeMillis()
                )
            )
        }
            .map {
                db.stickerZagDb().getStickerZagItemOrderBy()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ items ->
                Log.d("addRecent : items = ", items.toString())
                stickerAdapter.data = items
                stickerAdapter.notifyDataSetChanged()
            }, { error ->
                Log.d("addRecent : error = ", error.message)
            })
    }

    fun refreshData() {
        Log.d("refreshData() ","called")
        loadRecentList()
    }
}
