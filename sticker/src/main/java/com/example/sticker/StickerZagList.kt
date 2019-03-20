package com.example.sticker

import android.content.Context
import com.example.sticker.model.StickerZagData
import com.example.sticker.model.StickerZagListData
import com.google.gson.Gson
import java.io.IOException
import java.nio.charset.Charset

class StickerZagList @JvmOverloads constructor(
    private val context: Context
) {

    private fun loadJSONFromAsset(): String? {
        var json: String?
        try {
            val inputStream = context.assets.open("sticker_zag.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val charset: Charset = Charsets.UTF_8
            json = String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    fun getStickerZagList(): List<StickerZagData>? {
        val gson = Gson()
        var itemsList: List<StickerZagData>
        val stickerZagData = gson.fromJson(loadJSONFromAsset(), StickerZagListData::class.java)

        itemsList = stickerZagData.data as MutableList<StickerZagData>
        return itemsList
    }
}
