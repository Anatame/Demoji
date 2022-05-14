package com.anatame.crazyapp

import androidx.lifecycle.MutableLiveData

object CommonData{
    var message = ""
    var emoteMenuStatus: (EmoteMenu)-> Unit = {}
}

sealed class EmoteMenu {
    object Open: EmoteMenu()
    object Closed: EmoteMenu()
}