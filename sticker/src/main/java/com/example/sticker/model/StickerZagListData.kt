package com.example.sticker.model

import com.google.gson.annotations.SerializedName

class StickerZagListData {
    @SerializedName("data")
    var data: List<StickerZagData>? = null
}

class StickerZagData {

    @SerializedName("group")
    var group: String? = null
    @SerializedName("group_image")
    var groupImage: String? = null
    @SerializedName("group_extension")
    var groupExtension: String? = null
    @SerializedName("items")
    var items: List<StickerZagItemData>? = null
}
