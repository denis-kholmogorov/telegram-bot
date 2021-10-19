package com.zubayr.service.control.utils

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton


fun InlineKeyboardButton.setButton(textButton: String, path: String) = this.apply {
    text = textButton
    callbackData = path
}

fun MutableList<InlineKeyboardButton>.addButtonKubernetes(map: Map<String, String>) = this.also {

    map.forEach { (t, d) ->
        it.add(InlineKeyboardButton().apply {
            text = t
            callbackData = d
        })
    }
}