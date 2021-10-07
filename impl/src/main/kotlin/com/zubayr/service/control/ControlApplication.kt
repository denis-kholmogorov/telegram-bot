package com.zubayr.service.control

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ControlApplication {

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<ControlApplication>(*args)
		}
	}
}

