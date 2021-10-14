package com.zubayr.service.control.service

import io.fabric8.kubernetes.client.KubernetesClient

class DeploymentsService(
    val kubernetesClient: KubernetesClient
) {
    fun getDeployments(){
        kubernetesClient.apps().deployments()
    }
}