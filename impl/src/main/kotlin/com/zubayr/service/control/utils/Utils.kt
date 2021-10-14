package com.zubayr.service.control.utils

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton


fun InlineKeyboardButton.setButton(textButton: String, path: String) = this.apply {
    text = textButton
    callbackData = path
}