package com.zubayr.service.control.service

import com.zubayr.service.control.service.enum.BotAnswer
import io.fabric8.kubernetes.client.KubernetesClient
import org.apache.logging.log4j.kotlin.Logging
import org.jvnet.hk2.annotations.Service
import org.springframework.beans.factory.annotation.Value
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Service
class BotService(
        val kubernetesClient: KubernetesClient,
        val configMapService: ConfigMapService
) : TelegramLongPollingCommandBot(), Logging {


    @Value("\${telegram.bot.token}")
    private val token: String? = null

    @Value("\${telegram.bot.token}")
    private val botName: String? = null

    val frases = BotAnswer.values()

    override fun getBotToken() = token

    override fun getBotUsername() = botName

    override fun processNonCommandUpdate(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            val messageInput = update.message
            logger.info { "processing nonCommandUpdate $messageInput" }

            val messageOutput = SendMessage()
            when {
                messageInput.text.contains("/start") -> {

                    messageOutput.chatId = messageInput.chatId.toString()
                    messageOutput.text = BotAnswer.BELLO.answer + " ${messageInput.from.firstName}"
                    //execute(SendSticker(messageInput.chatId.toString(), InputFile(File(stiker.random()))))
                    execute(messageOutput.apply { replyMarkup = createButton() })
                    logger.info { "answer ${messageOutput.text}" }
                }
                messageInput.text.startsWith("/bye") -> {
                    //  execute(SendSticker(messageInput.chatId.toString(), InputFile(File("impl/src/main/resources/buy.webp"))))
                    messageOutput.chatId = messageInput.chatId.toString()
                    messageOutput.text = BotAnswer.HASTA_LA_VISTA.answer + " ${messageInput.from.firstName}"
                    logger.info { "answer ${messageOutput.text}" }

                }
                else -> {
                    messageOutput.chatId = messageInput.chatId.toString()
                    messageOutput.text = frases.random().answer
                    execute(messageOutput)
                    logger.info { "answer ${messageOutput.text}" }
                }
            }
        } else if (update.hasCallbackQuery()) {
            when {
                update.callbackQuery.data.startsWith("/speak") -> {
                    execute(SendMessage().apply {
                        chatId = update.getId()
                        text = frases.random().answer
                    })
                }
                update.callbackQuery.data.startsWith("/bye") -> {
                    execute(SendMessage().apply {
                        chatId = update.getId()
                        text = BotAnswer.HASTA_LA_VISTA.answer + " ${update.callbackQuery.message.from.firstName}"
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