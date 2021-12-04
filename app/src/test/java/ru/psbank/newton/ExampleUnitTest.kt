package ru.psbank.newton

import org.json.JSONObject
import org.junit.Test

import org.junit.Assert.*
import ru.psbank.newton.util.api.Auth
import ru.psbank.newton.util.api.GetMe

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    companion object {
        var token = ""
    }

    @Test
    fun auth() {
        token = ""
        val body = Auth("antonckya", "ilovepenis").post().body
        println(body)
        token = JSONObject(body).getJSONObject("token").getString("value")
    }

    @Test
    fun getMe() {
        assert(token != "")
        println(GetMe(token).get().body)
    }
}