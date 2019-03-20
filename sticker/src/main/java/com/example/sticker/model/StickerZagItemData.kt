package com.example.sticker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "sticker_zag")
data class StickerZagItemData (
    @PrimaryKey
    @ColumnInfo(name = "code")
    @field:SerializedName("code")
    var code: String,

    @ColumnInfo(name = "name")
    @field:SerializedName("name")
    var name: String?,

    @ColumnInfo(name = "image")
    @field:SerializedName("image")
    var image: String?,

    @ColumnInfo(name = "imageExtension")
    @field:SerializedName("image_extension")
    var imageExtension: String?,

    @ColumnInfo(name = "created_date")
    var createDate: Long?,

    @ColumnInfo(name = "group")
    var group: String?
)
