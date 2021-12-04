package ru.psbank.newton.ui.adaptation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.fragment_adaptation.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.psbank.newton.App
import ru.psbank.newton.R
import ru.psbank.newton.util.api.GetTasks

class AdaptationFragment : Fragment(R.layout.fragment_adaptation) {

    private lateinit var maintasksController: MaintasksController
    private var progressValue = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!this::maintasksController.isInitialized) {
            maintasksController = MaintasksController(this)
            updatePage()
        }
        adaptation_refresh.setOnRefreshListener {
            updatePage()
        }

        maintasks_recycler?.adapter = maintasksController.adapter
        maintasks_recycler?.layoutManager = LinearLayoutManager(context)
    }

    override fun onResume() {
        super.onResume()
        if ((activity?.application as App).preferenceRepository.refreshMain) {
            updatePage()
            (activity?.application as App).preferenceRepository.refreshMain = false
        } else if (progressValue == -1) {
            progress?.animateProgress(100, 0, 0)
            progress_num.setText("0%")
        } else {
            progress?.animateProgress(100, 0, progressValue)
            progress_num.setText("$progressValue%")
        }
        adaptation_refresh.isRefreshing = false
    }

    private fun updatePage() {
        adaptation_refresh.isRefreshing = true
        GlobalScope.launch {
            val response =
                GetTasks((activity?.application as App).preferenceRepository.token).get()

            when (response.code) {
                200 -> {
                    val responseObject = JSONObject(response.body)

                    progressValue = (responseObject.getInt("amount_done_tasks")
                        .toFloat() / responseObject.getInt("amount_tasks") * 100).toInt()
                    MainScope().launch {
                        progress?.animateProgress(100, 0, progressValue)
                        progress_num.setText("$progressValue%")
                    }

                    val maintasksObject = responseObject.getJSONObject("maintasks")

                    var tempObject: JSONObject
                    val maintasksList = arrayListOf<Triple<String, String, Boolean>>()
                    for (i in 1..10000) {
                        if (maintasksObject.has("$i")) {
                            tempObject = maintasksObject.getJSONObject("$i")
                            maintasksList.add(
                                Triple(
                                    tempObject.getString("title"),
                                    tempObject.getJSONObject("subtasks").toString(),
                                    tempObject.getInt("amount_tasks") == tempObject.getInt("amount_done_tasks")
                                )
                            )
                        }
                    }
                    MainScope().launch {
                        maintasksController.items = maintasksList
                        adaptation_refresh.isRefreshing = false
                    }
                }
                else -> {
                    MainScope().launch {
                        Toast.makeText(
                            context,
                            "Произошла неожиданная ошибка. Попытка переподключения через 5 секунд.",
                            Toast.LENGTH_SHORT
                        ).show()
                        adaptation_refresh.isRefreshing = false
                    }
                }
            }
        }
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