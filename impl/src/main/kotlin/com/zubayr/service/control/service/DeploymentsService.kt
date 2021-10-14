package com.zubayr.service.control.service

import com.zubayr.service.control.constants.ONE_DEPLOYMENT
import com.zubayr.service.control.constants.ONE_MAP
import com.zubayr.service.control.utils.setButton
import io.fabric8.kubernetes.client.KubernetesClient
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import java.util.logging.Logger

@Service
class DeploymentsService(
    val kubernetesClient: KubernetesClient
): Logging {

    fun getDeployments() = InlineKeyboardMarkup().apply {
        keyboard = mutableListOf(createListDeployments())
    }

    fun getOneDeployment() = ""

    private fun createListDeployments() = kubernetesClient
        .apps()
        .deployments()
        .list()
        .items
        .map { it.metadata.name }
        .map {
            logger.info("Deployment name - $it")
            InlineKeyboardButton().setButton(it, ONE_DEPLOYMENT + it)
        }
}

