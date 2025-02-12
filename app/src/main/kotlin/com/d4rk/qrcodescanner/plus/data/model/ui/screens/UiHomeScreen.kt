package com.d4rk.qrcodescanner.plus.data.model.ui.screens

data class UiHomeScreen(
    val lessons: ArrayList<UiHomeLesson> = ArrayList()
)

data class UiHomeLesson(
    val lessonId: String = "",
    val lessonTitle: String = "",
    val lessonDescription: String = "",
    val lessonType: String = "",
    val lessonTags: List<String> = emptyList(),
    val thumbnailImageUrl: String = "",
    val squareImageUrl: String = "",
    val deepLinkPath: String = "",
    var isFavorite: Boolean = false
)