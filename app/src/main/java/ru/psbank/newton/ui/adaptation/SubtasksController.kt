package ru.psbank.newton.ui.adaptation

import androidx.fragment.app.Fragment
import com.airbnb.epoxy.EpoxyController
import org.json.JSONObject
import ru.psbank.newton.data.entities.Task

class SubtasksController(val fragment: Fragment) : EpoxyController() {

    var items: ArrayList<Triple<String, String, Boolean>> = arrayListOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        var k = 0
        items.forEach {
            subtask(fragment) {
                id(k++)
                subTaskName(it.first)
                dayTasks(it.second)
                subTaskFinished(it.third)
            }
        }
    }
}