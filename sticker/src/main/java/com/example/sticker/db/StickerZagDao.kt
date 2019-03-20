package com.example.sticker.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sticker.model.StickerZagItemData

@Dao
interface StickerZagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStickerZag(item: StickerZagItemData)

    @Query("UPDATE sticker_zag SET created_date=:updateDate WHERE code = :code")
    fun update(updateDate: Long, code: String)

    @Query("SELECT * FROM sticker_zag")
    fun getStickerZagItem(): List<StickerZagItemData>

    @Query("SELECT * FROM sticker_zag ORDER BY created_date DESC LIMIT 36")
    fun getStickerZagItemOrderBy(): List<StickerZagItemData>

    @Query("DELETE FROM sticker_zag")
    fun deleteAll()
}
