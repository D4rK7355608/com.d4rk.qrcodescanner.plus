package com.d4rk.qrcodescanner.plus.model

enum class SearchEngine(val templateUrl : String) {
    NONE(templateUrl = "") , ASK_EVERY_TIME(templateUrl = "") , BING(templateUrl = "https://www.bing.com/search?q=") , DUCK_DUCK_GO(
        templateUrl = "https://duckduckgo.com/?q="
    ) ,
    GOOGLE(
        templateUrl = "https://www.google.com/search?q="
    ) ,
    STARTPAGE(templateUrl = "https://www.startpage.com/sp/search?query=") , QWANT(templateUrl = "https://www.qwant.com/?q=") , YAHOO(
        templateUrl = "https://search.yahoo.com/search?p="
    ) ,
    YANDEX(templateUrl = "https://www.yandex.ru/search/?text=") ,
}