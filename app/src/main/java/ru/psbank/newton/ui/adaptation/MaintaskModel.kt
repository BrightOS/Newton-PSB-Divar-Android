package ru.psbank.newton.ui.adaptation

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.EpoxyRecyclerView
import org.json.JSONObject
import ru.psbank.newton.R
import ru.psbank.newton.util.KotlinHolder

@EpoxyModelClass(layout = R.layout.item_maintask)
abstract class MaintaskModel(val fragment: Fragment) :
    EpoxyModelWithHolder<MaintaskModel.Holder>() {

    @EpoxyAttribute
    lateinit var mainTaskName: String

    @EpoxyAttribute
    var mainTaskFinished = false

    @EpoxyAttribute
    lateinit var subTasks: String

    private lateinit var subtasksController: SubtasksController

    override fun bind(holder: Holder) {
        holder.title.text = mainTaskName

        if (!this::subtasksController.isInitialized)
            subtasksController = SubtasksController(fragment)

        var i = 1
        val subTasksObject = JSONObject(subTasks)
        println(subTasks)
        var tempSubTask: JSONObject
        val subTasksList = arrayListOf<Triple<String, String, Boolean>>()
        for (i in 1..10000)
            if (subTasksObject.has("$i")) {
                tempSubTask = subTasksObject.getJSONObject("$i")
                subTasksList.add(
                    Triple(
                        tempSubTask.getString("title"),
                        tempSubTask.getJSONObject("tasks").toString(),
                        tempSubTask.getInt("amount_tasks") == tempSubTask.getInt("amount_done_tasks")
                    )
                )
                println("${tempSubTask.getString("title")} ${tempSubTask.getInt("amount_tasks")}")
                println("${tempSubTask.getString("title")} ${tempSubTask.getInt("amount_done_tasks")}")
            }

        println(subTasksList.size)

        holder.subtasksRecycler.adapter = subtasksController.adapter
        holder.subtasksRecycler.layoutManager = LinearLayoutManager(fragment.context)
        subtasksController.items = subTasksList
    }

    inner class Holder : KotlinHolder() {
        val title by bind<TextView>(R.id.title)
        val subtasksRecycler by bind<EpoxyRecyclerView>(R.id.subtasks_recycler)
    }
}