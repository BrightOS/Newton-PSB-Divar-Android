package ru.psbank.newton.util.api

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import ru.psbank.newton.data.entities.PSBResponse

class Auth(login: String, password: String) {
    private val _jsonObject =
        JSONObject(mapOf("login" to login, "password" to password))

    fun post(): PSBResponse =
        runBlocking {
            return@runBlocking GlobalScope.async {
                APIUtils.post("methods/auth", _jsonObject, null, APIUtils.URL_ENCODED)
            }.await()
        }
}