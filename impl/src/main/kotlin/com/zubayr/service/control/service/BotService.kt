package com.zubayr.service.control.service

import com.zubayr.service.control.constants.CONFIG_MAPS
import com.zubayr.service.control.constants.ONE_MAP
import io.fabric8.kubernetes.client.KubernetesClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

@Service
class BotService(
    val kubernetesClient: KubernetesClient,
    val configMapService: ConfigMapService
) : TelegramLongPollingCommandBot() {


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
            val messageOutput = SendMessage()
            when {
                messageInput.text.contains("/start") -> {

                    messageOutput.chatId = messageInput.chatId.toString()
                    messageOutput.text = "Белло! ${messageInput.from.firstName}"
                    //execute(SendSticker(messageInput.chatId.toString(), InputFile(File(stiker.random()))))
                    execute(messageOutput.apply { replyMarkup = createButton() })
                }
                messageInput.text.contains(CONFIG_MAPS) -> {
                    messageOutput.chatId = messageInput.chatId.toString()
                    messageOutput.text = "Матока! Вот все Мапы"
                    messageOutput.replyMarkup = configMapService.getAllConfigMaps()
                    //execute(SendSticker(messageInput.chatId.toString(), InputFile(File(stiker.random()))))
                    execute(messageOutput)
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