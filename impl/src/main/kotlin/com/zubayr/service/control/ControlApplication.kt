package com.zubayr.service.control

import com.zubayr.service.control.service.BotService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@SpringBootApplication
class ControlApplication {

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<ControlApplication>(*args)
            TelegramBotsApi(DefaultBotSession::class.java)
            .registerBot(BotService())
		}
	}
}

