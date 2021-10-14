package com.zubayr.service.control.service

import com.zubayr.service.control.constants.ONE_MAP
import com.zubayr.service.control.utils.setButton
import io.fabric8.kubernetes.client.KubernetesClient
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import java.util.logging.Logger

@Service
class ConfigMapService(
    val kubernetesClient: KubernetesClient
) {

    fun getAllConfigMaps() = InlineKeyboardMarkup().apply {
        keyboard = mutableListOf(createListMaps())
    }

    fun getConfigMapByName(name: String) = name.let {
        val nameConfigMap = it.substringAfter(ONE_MAP)
        Logger.getGlobal().info("NameConfigMap = $it")
        kubernetesClient
            .configMaps()
            .withName(nameConfigMap)
            .get()
            .data
            .toString()
    }

    private fun createListMaps() = kubernetesClient
        .configMaps()
        .list()
        .items
        .map { it.metadata.name }
        .map {
            Logger.getGlobal().info("textButton = $it")
            InlineKeyboardButton().setButton(it, ONE_MAP + it)
        }


}