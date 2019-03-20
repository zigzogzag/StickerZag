package com.example.stickerzag

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.sticker.PreviewSticker
import com.example.sticker.StickerZagGridView
import com.example.sticker.StickerZagPopup
import com.example.sticker.model.StickerZagItemData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), StickerZagGridView.OnStickerZagClickedListener {

    internal lateinit var popup: StickerZagPopup
    private var longClick = false
    private lateinit var popupPreview: PreviewSticker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()

        sticker_btn.setOnClickListener(onClickBtn())
    }

    private fun setupView() {
        val rootView = findViewById<ViewGroup>(android.R.id.content)
        popup = StickerZagPopup(this)
        popup.setRootView(rootView)
        popupPreview = PreviewSticker(this, rootView)
        popup.setBackgroundDrawable(null)
        popupPreview.setBackgroundDrawable(null)
        popup.setSizeForSoftKeyboard()
        popup.setOnSoftKeyboardOpenCloseListener(object : StickerZagPopup.OnSoftKeyboardOpenCloseListener {
            override fun onKeyboardOpen(keyBoardHeight: Int) {
            }

            override fun onKeyboardClose() {
                if (popup.isShowing) {
                    popup.dismiss()
                }
            }
        })

        /*KeyboardUtils.addKeyboardToggleListener(
            this
        ) { isVisible ->
            if (isVisible) {
                layout_bb_code.show()
            } else {
                layout_bb_code.hide()
            }
        }*/
    }

    private fun onClickBtn(): View.OnClickListener? {
        return View.OnClickListener {
            if (!popup.isShowing) {
                if (popup.isKeyBoardOpen()!!) {
                    popup.showAtBottom()
                } else {
                    editText.isFocusableInTouchMode = true
                    editText.requestFocus()
                    popup.showAtBottomPending()
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.showSoftInput(
                        editText,
                        InputMethodManager.SHOW_IMPLICIT
                    )
                }
            } else {
                popup.dismiss()
            }
        }
    }

    override fun onStickerZagClicked(item: StickerZagItemData) {
        longClick = false

    }

    override fun onStickerZagLongClicked(item: StickerZagItemData, group: String) {
        longClick = true
        popupPreview.setPreview(group, item)
    }

    override fun onStickerZagTouchClicked(item: StickerZagItemData, event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_UP) {
            if (longClick) {
                popupPreview.dismiss()
            }
        }

        if (event.action == MotionEvent.ACTION_CANCEL) {
            if (longClick) {
                popupPreview.dismiss()
            }
        }

    }
}
