package com.zubayr.service.control.service

import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendSticker
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import java.io.File


class BotService : TelegramLongPollingCommandBot() {

    val frases = mutableListOf(
        "Матока", "Бананонина!", "Белло!", "Пупай!", "Пара ту", "По тае то пара ту",
        "Ти амо пупай!", "Лук ат ту", "Папой, теремика папой? а папой"
    )

    val stiker = mutableListOf(
        "impl/src/main/resources/minon.webp",
        "impl/src/main/resources/hello.webp"
    )



    override fun getBotToken() = "2083240655:AAEm_7zF45avmHrQHvT1HuhWNbk4uyvVWZE"

    override fun getBotUsername() = "minion"

    override fun processNonCommandUpdate(update: Update) {
        if(update.hasMessage() && update.message.hasText()) {
            val messageInput = update.message
            val messageOutput = SendMessage()
            when {
                messageInput.text.contains("/start") -> {

                    messageOutput.chatId = messageInput.chatId.toString()
                    messageOutput.text = "Белло! ${messageInput.from.firstName}"
                    //execute(SendSticker(messageInput.chatId.toString(), InputFile(File(stiker.random()))))
                    execute(messageOutput.apply { replyMarkup = createButton() })
                }
                messageInput.text.startsWith("/bye") -> {
                    execute(SendSticker(messageInput.chatId.toString(), InputFile(File("impl/src/main/resources/buy.webp"))))
                }
                else -> {
                    messageOutput.chatId = messageInput.chatId.toString()
                    messageOutput.text = frases.random()
                    execute(messageOutput)
                }
            }
        } else if(update.hasCallbackQuery()){
            when{
                update.callbackQuery.data.startsWith("/speak") -> {
                    execute(SendMessage().apply {
                        chatId = update.getId()
                        text = frases.random()
                    })
                }
                update.callbackQuery.data.startsWith("/bye") -> {
                    execute(SendSticker(update.getId(), InputFile(File("impl/src/main/resources/buy.webp"))))
                }
            }
        }
    }

    private fun createButton(): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()
        val buttonSpeak = InlineKeyboardButton().apply {
            text = "Поболтаем?"
            callbackData = "/speak"
        }
        val buttonBye = InlineKeyboardButton().apply {
            text = "Пока"
            callbackData = "/bye"
        }
        val keyboardButtonsRow1 = mutableListOf(
            buttonSpeak,
            buttonBye
        )
        inlineKeyboardMarkup.keyboard = mutableListOf(keyboardButtonsRow1)
        return inlineKeyboardMarkup
    }

    private fun Update.getId() = this.callbackQuery.message.chatId.toString()
}