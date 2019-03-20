package com.example.sticker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sticker.model.StickerZagItemData

@Database(
    entities = [StickerZagItemData::class],
    version = 1,
    exportSchema = false
)
abstract class StickerZagDb : RoomDatabase() {
    abstract fun stickerZagDb(): StickerZagDao

    companion object {
        fun create(context: Context): StickerZagDb {
            return Room.databaseBuilder(context, StickerZagDb::class.java, "stickerzag.db")
                .build()
        }
    }
}
