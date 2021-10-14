package com.zubayr.service.control.service

import com.zubayr.service.control.constants.CONFIG_MAPS
import com.zubayr.service.control.constants.DEPLOYMENTS
import com.zubayr.service.control.constants.ONE_MAP
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Service
class BotService(
    private val configMapService: ConfigMapService,
    private val deploymentsService: DeploymentsService
) : TelegramLongPollingCommandBot(), Logging {


    @Value("\${telegram.bot.token}")
    private val token: String? = null

    @Value("\${telegram.bot.token}")
    private val botName: String? = null

    val frases = mutableListOf(
        "Матока", "Бананонина!", "Белло!", "Пупай!", "Пара ту", "По тае то пара ту",
        "Ти амо пупай!", "Лук ат ту", "Папой, теремика папой? а папой"
    )

    override fun getBotToken() = token

    override fun getBotUsername() = botName

    override fun processNonCommandUpdate(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            val messageInput = update.message
            logger.info { "processing nonCommandUpdate $messageInput" }

            val messageOutput = SendMessage()
            when {
                messageInput.text.contains("/hello") -> {

                    messageOutput.chatId = messageInput.chatId.toString()
                    messageOutput.text = "Белло! ${messageInput.from.firstName}"
                    execute(messageOutput.apply { replyMarkup = createButton() })
                    logger.info { "answer ${messageOutput.text}" }
                }

                messageInput.text.startsWith("/bye") -> {
                    messageOutput.chatId = messageInput.chatId.toString()
                    messageOutput.text = "Hasta la vista! Baby ${messageInput.from.firstName}"
                    logger.info { "answer ${messageOutput.text}" }

                }
                else -> {
                    messageOutput.chatId = messageInput.chatId.toString()
                    messageOutput.text = frases.random()
                    execute(messageOutput)
                    logger.info { "answer ${messageOutput.text}" }
                }
            }
        } else if (update.hasCallbackQuery()) {
            when {
                update.callbackQuery.data.startsWith("/speak") -> {
                    execute(SendMessage().apply {
                        chatId = update.getId()
                        text = frases.random()
                    })
                }
                update.callbackQuery.data.startsWith(ONE_MAP) -> {
                    execute(SendMessage().apply {
                        chatId = update.getId()
                        text = configMapService.getConfigMapByName(update.callbackQuery.data)
                    })
                }
                update.callbackQuery.data.startsWith(DEPLOYMENTS) -> {
                    execute(SendMessage().apply {
                        chatId = update.getId()
                        text = "Банана! Держи деплойменты"
                        replyMarkup = deploymentsService.getDeployments()
                    })
                }
                update.callbackQuery.data.startsWith(CONFIG_MAPS) -> {
                    execute(SendMessage().apply {
                        chatId = update.getId()
                        text = "Матока! Вот все Мапы"
                        replyMarkup = configMapService.getAllConfigMaps()
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
        val buttonConfigMap = InlineKeyboardButton().apply {
            text = "Конфиг мапы"
            callbackData = CONFIG_MAPS
        }
        val buttonDeployment = InlineKeyboardButton().apply {
            text = "Деплойменты"
            callbackData = DEPLOYMENTS
        }
        val buttonBye = InlineKeyboardButton().apply {
            text = "Пока"
            callbackData = "/bye"
        }
        val keyboardButtonsRow1 = mutableListOf(
            buttonConfigMap,
            buttonDeployment,
            buttonBye
        )
        inlineKeyboardMarkup.keyboard = mutableListOf(keyboardButtonsRow1)
        return inlineKeyboardMarkup
    }

    private fun Update.getId() = this.callbackQuery.message.chatId.toString()
}