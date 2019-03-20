package com.example.sticker

import com.example.sticker.model.StickerZagItemData

interface StickerZagRecent {
    fun addRecent(item: StickerZagItemData, group: String?)
}
