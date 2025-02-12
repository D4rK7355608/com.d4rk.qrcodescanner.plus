package com.d4rk.qrcodescanner.plus.data.model.ui.screens

data class UiLessonScreen(
    val lessonTitle : String = "" , val lessonContent : ArrayList<UiLessonContent> = ArrayList()
)

data class UiLessonContent(
    val contentId : String = "" ,
    val contentType : String = "" ,
    val contentText : String = "" ,
    val contentCode : String = "" ,
    val programmingLanguage : String = "" ,
    val contentImageUrl : String = ""
)