package ru.psbank.newton.ui.adaptation

import androidx.fragment.app.Fragment
import com.airbnb.epoxy.EpoxyController

class MaintasksController(val fragment: Fragment) : EpoxyController() {

    var items: ArrayList<Triple<String, String, Boolean>> = arrayListOf()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        var k = 0
        items.forEach {
            maintask(fragment) {
                id(k++)
                mainTaskName(it.first)
                subTasks(it.second)
                mainTaskFinished(it.third)
            }
        }
    }
}