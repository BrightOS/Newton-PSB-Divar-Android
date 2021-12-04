package ru.psbank.newton.ui.adaptation.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.fragment_tasks.*
import org.json.JSONObject
import ru.psbank.newton.App
import ru.psbank.newton.R
import ru.psbank.newton.data.entities.Task

class TasksFragment : Fragment(R.layout.fragment_tasks) {

    private val args: TasksFragmentArgs? by navArgs()
    private lateinit var tasksController: TasksController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println(args!!.objects)

        val tasksObject = JSONObject(args!!.objects)
        val tasksList = arrayListOf<Task>()

        if (!this::tasksController.isInitialized)
            tasksController = TasksController(this)

        tasks_appbar?.setStartButtonOnClickListener {
            findNavController().popBackStack()
        }

        tasks_appbar?.setText(args!!.title)

        for (i in 1..10000)
            if (tasksObject.has("$i"))
                tasksObject.getJSONObject("$i").let {
                    tasksList.add(
                        Task(
                            it.getString("title"),
                            it.getString("desc"),
                            it.getInt("id"),
                            it.getBoolean("is_done")
                        )
                    )
                }

        tasks_recycler?.layoutManager = LinearLayoutManager(context)
        tasks_recycler?.adapter = tasksController.adapter

        tasksController.items = tasksList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if ((requireActivity().application as App).preferenceRepository.animations) {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
            returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        }
    }
}