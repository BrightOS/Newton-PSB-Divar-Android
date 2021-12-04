package ru.psbank.newton.ui.adaptation.tasks

import androidx.fragment.app.Fragment
import com.airbnb.epoxy.EpoxyController
import ru.psbank.newton.data.PreferenceRepository
import ru.psbank.newton.data.entities.Task

class TasksController(val fragment: Fragment) : EpoxyController() {

    var items: ArrayList<Task> = arrayListOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        var k = 0
        items.forEach {
            task(fragment) {
                id(k++)
                taskID(it.taskID)
                title(it.title)
                description(it.description)
                checked(it.checked)
            }
        }
    }
}