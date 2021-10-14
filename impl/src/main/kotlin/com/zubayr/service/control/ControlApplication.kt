package com.zubayr.service.control

import com.zubayr.service.control.service.BotService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import javax.annotation.PostConstruct


@SpringBootApplication
class ControlApplication {
	@Autowired
	val context: ApplicationContext? = null

	@PostConstruct
	fun startTelegram(){
		TelegramBotsApi(DefaultBotSession::class.java)
			.registerBot(context?.getBean(BotService::class.java))
	}

	companion object {

		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<ControlApplication>(*args)
		}
	}
}

