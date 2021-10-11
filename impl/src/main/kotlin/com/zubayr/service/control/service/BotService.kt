package com.zubayr.service.control.service

import org.springframework.beans.factory.annotation.Value
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton


class BotService : TelegramLongPollingCommandBot() {

    @Value("\${telegram.bot.token}")
    private val token: String? = null

    @Value("\${telegram.bot.token}")
    private val botName: String? = null

    val frases = mutableListOf(
        "Матока", "Бананонина!", "Белло!", "Пупай!", "Пара ту", "По тае то пара ту",
        "Ти амо пупай!", "Лук ат ту", "Папой, теремика папой? а папой"
    )

    val stiker = mutableListOf(
        "impl/src/main/resources/minon.webp",
        "impl/src/main/resources/hello.webp"
    )

    override fun getBotToken() = token

    override fun getBotUsername() = botName

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
                  //  execute(SendSticker(messageInput.chatId.toString(), InputFile(File("impl/src/main/resources/buy.webp"))))
                    messageOutput.chatId = messageInput.chatId.toString()
                    messageOutput.text = "Hasta la vista! Baby ${messageInput.from.firstName}"
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
                    execute(SendMessage().apply {
                        chatId = update.getId()
                        text = "Hasta la vista! Baby ${update.callbackQuery.message.from.firstName}"
                    })
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