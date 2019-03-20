package com.example.sticker

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.Log
import androidx.viewpager.widget.ViewPager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.example.sticker.adapter.StickerZagPagerAdapter
import com.example.sticker.model.StickerZagItemData
import com.google.android.material.tabs.TabLayout

class StickerZagPopup(private val context: Context) : PopupWindow(context), StickerZagRecent {

    private var keyBoardHeight = 0
    private var pendingOpen = false
    private var isOpened = false

    private var _rootView: View? = null

    private lateinit var adapter: StickerZagPagerAdapter

    private lateinit var viewPager: ViewPager

    private var onSoftKeyboardOpenCloseListener: OnSoftKeyboardOpenCloseListener? = null

    init {
        val customView = createCustomView()
        contentView = customView
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        setSize(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }

    fun setRootView(view: View) {
        this._rootView = view
    }

    private fun createCustomView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.sticker_zag_keyboard, null, false)
        viewPager = view.findViewById(R.id.pager)
        val tab = view.findViewById<StickerZagTabLayout>(R.id.tab_sticker_zag)
        val ivKeyboard = view.findViewById<LinearLayout>(R.id.iv_keyboard)
        val sticker = StickerZagList(context)
        val gridView = mutableListOf<StickerZagGridView>()
        val stickerSize = sticker.getStickerZagList()!!.size
        val iconList = mutableListOf<String>()

        iconList.add(0, "ic_tab_recent")
        gridView.add(StickerZagRecentGridView(context, this))

        Log.d("size", stickerSize.toString())

        for (i in 0 until stickerSize) {
            iconList.add(sticker.getStickerZagList()!![i].groupImage!!)
            gridView.add(StickerZagGridView(context, sticker.getStickerZagList()!![i], this))
        }

        adapter = StickerZagPagerAdapter(gridView)

        tab.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab))

        viewPager.adapter = adapter
        tab.setupCustomView(adapter, iconList)

        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                if (p0!!.position == 0) {
                    (viewPager.adapter as? StickerZagPagerAdapter)?.getRecentFragment()
                        ?.refreshData()
                }
            }
        })

        ivKeyboard.setOnClickListener {
            dismiss()
        }

        return view
    }

    fun setOnSoftKeyboardOpenCloseListener(listener: OnSoftKeyboardOpenCloseListener) {
        this.onSoftKeyboardOpenCloseListener = listener
    }

    private fun setSize(width: Int, height: Int) {
        setWidth(width)
        setHeight(height)
    }

    fun setSizeForSoftKeyboard() {
        _rootView?.let { rootView ->
            rootView.viewTreeObserver.addOnGlobalLayoutListener {
                val r = Rect()
                rootView.getWindowVisibleDisplayFrame(r)

                val screenHeight = getUsableScreenHeight()
                var heightDifference = screenHeight - (r.bottom - r.top)

                val resourceId =
                    context.resources.getIdentifier("status_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    heightDifference -= context.resources
                        .getDimensionPixelSize(resourceId)
                }

                if (heightDifference > 100) {
                    keyBoardHeight = heightDifference
                    setSize(WindowManager.LayoutParams.MATCH_PARENT, keyBoardHeight)
                    if (!isOpened) {
                        if (onSoftKeyboardOpenCloseListener != null)
                            onSoftKeyboardOpenCloseListener!!.onKeyboardOpen(keyBoardHeight)
                    }
                    isOpened = true
                    if (pendingOpen) {
                        showAtBottom()
                        pendingOpen = false
                    }
                } else {
                    isOpened = false
                    if (onSoftKeyboardOpenCloseListener != null)
                        onSoftKeyboardOpenCloseListener!!.onKeyboardClose()
                }
            }
        }
    }

    private fun getUsableScreenHeight(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.resources.displayMetrics.heightPixels
        } else {
            _rootView?.rootView?.height ?: 0
        }
    }

    fun showAtBottom() {
        showAtLocation(_rootView, Gravity.BOTTOM, 0, 0)
    }

    fun showAtBottomPending() {
        if (this.isKeyBoardOpen()!!)
            showAtBottom()
        else {
            pendingOpen = true
        }
    }

    fun isKeyBoardOpen(): Boolean? {
        return isOpened
    }

    override fun addRecent(item: StickerZagItemData, group: String?) {
        (viewPager.adapter as? StickerZagPagerAdapter)?.getRecentFragment()
            ?.addRecent(item, group)
    }

    interface OnSoftKeyboardOpenCloseListener {
        fun onKeyboardOpen(keyBoardHeight: Int)
        fun onKeyboardClose()
    }
}
