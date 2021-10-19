package com.zubayr.service.control.service

import com.zubayr.service.control.constants.*
import com.zubayr.service.control.utils.addButtonKubernetes
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

    @Value("\${available-names}")
    private val names: List<String> = ArrayList()

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
                    execute(messageOutput.apply { replyMarkup = createButton(messageInput.from.userName) })
                    logger.info { "processNonCommandUpdate() /hello - answer ${messageOutput.text}" }
                }

                messageInput.text.startsWith("/bye") -> {
                    messageOutput.chatId = messageInput.chatId.toString()
                    messageOutput.text = "Hasta la vista! Baby ${messageInput.from.firstName}"
                    logger.info { "processNonCommandUpdate() /bye - answer ${messageOutput.text}" }

                }
                else -> {
                    messageOutput.chatId = messageInput.chatId.toString()
                    messageOutput.text = frases.random()
                    execute(messageOutput)
                    logger.info { "processNonCommandUpdate() else - answer ${messageOutput.text}" }
                }
            }
        } else if (update.hasCallbackQuery()) {
            when {
                update.callbackQuery.data.startsWith(ONE_MAP) -> {
                    execute(SendMessage().apply {
                        chatId = update.getId()
                        text = configMapService.getConfigMapByName(update.callbackQuery.data)
                    })
                }
                update.callbackQuery.data.startsWith(CONFIG_MAPS) -> {
                    execute(SendMessage().apply {
                        chatId = update.getId()
                        text = "Матока! Вот все Мапы"
                        replyMarkup = configMapService.getAllConfigMaps()
                    })
                }
                update.callbackQuery.data.startsWith(DEPLOYMENTS) -> {
                    execute(SendMessage().apply {
                        chatId = update.getId()
                        text = "Банана! Держи деплойменты"
                        replyMarkup = deploymentsService.getDeployments()
                    })
                }
                update.callbackQuery.data.startsWith(ONE_DEPLOYMENT) -> {
                    execute(SendMessage().apply {
                        chatId = update.getId()
                        text = "Перезапуск деплоймента ${update.callbackQuery.data.substringAfter(ONE_DEPLOYMENT)}"
                        deploymentsService.getOneDeployment(update.callbackQuery.data)
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

    private fun createButton(name: String): InlineKeyboardMarkup {
        val inlineKeyboardMarkup = InlineKeyboardMarkup()

        val buttonBye = InlineKeyboardButton().apply {
            text = "Пока"
            callbackData = "/bye"
        }
        val keyboardButtonsRow = mutableListOf(buttonBye)
        if(names.contains(name)){
           keyboardButtonsRow.addButtonKubernetes(
               mapOf(
                   "Конфиг мапы" to CONFIG_MAPS,
                   "Деплойменты" to DEPLOYMENTS
               )
           )
        }
        inlineKeyboardMarkup.keyboard = mutableListOf(keyboardButtonsRow)
        return inlineKeyboardMarkup
    }


    private fun Update.getId() = this.callbackQuery.message.chatId.toString()
}