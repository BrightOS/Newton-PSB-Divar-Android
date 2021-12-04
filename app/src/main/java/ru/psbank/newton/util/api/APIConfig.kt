package ru.psbank.newton.util.api

class APIConfig {
    companion object {
        private const val connectionType = "http"
        private const val domain = "31.172.66.226:8080"
        const val errorCode = 7012020
        const val url = "$connectionType://$domain/"
    }
}