package ru.psbank.newton.util.api

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import ru.psbank.newton.data.entities.PSBResponse

class GetProject(val token: String) {

    fun get(): PSBResponse =
        runBlocking {
            GlobalScope.async {
                APIUtils.get("methods/myproject", token)
            }.await()
        }
}