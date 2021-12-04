package ru.psbank.newton.ui.project

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_project.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import ru.psbank.newton.App
import ru.psbank.newton.R
import ru.psbank.newton.util.api.GetProject

class ProjectFragment : Fragment(R.layout.fragment_project) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadProject()

        project_refresh?.setOnRefreshListener {
            loadProject()
        }
    }

    private fun loadProject() {
        GlobalScope.launch {
            val response =
                GetProject((requireActivity().application as App).preferenceRepository.token).get()

            when (response.code) {
                200 -> {
                    MainScope().launch {
                        val responseObject = JSONObject(response.body)
                        title?.text = responseObject.getString("title")
                        responseObject.getString("desc").let {
                            if (it == "null")
                                description?.visibility = View.GONE
                            else
                                description?.text = it
                        }
                        instruments?.text = responseObject.getString("instruments_desc")

                        val sourcesArray = responseObject.getJSONArray("sources")
                        sources?.withModels {
                            for (i in 0 until sourcesArray.length()) {
                                (sourcesArray.get(i) as JSONObject).let { src ->
                                    source(this@ProjectFragment) {
                                        id(i)
                                        title(src.getString("title"))
                                        url(src.getString("url"))
                                    }
                                }
                            }
                        }

                        val goalsArray = responseObject.getJSONArray("goals")
                        goals?.withModels {
                            for (i in 0 until goalsArray.length()) {
                                (goalsArray.get(i) as JSONObject).let { src ->
                                    goal {
                                        id(i)
                                        title(src.getString("title"))
                                        subtitle(src.getString("desc"))
                                    }
                                }
                            }
                        }

                        val participantsArray = responseObject.getJSONArray("participants")
                        participants?.withModels {
                            for (i in 0 until participantsArray.length()) {
                                (participantsArray.get(i) as JSONObject).let { src ->
                                    participant {
                                        id(i)
                                        title(
                                            "${src.getString("second_name")} ${src.getString("first_name")} ${
                                                src.getString(
                                                    "third_name"
                                                )
                                            }"
                                        )
                                        description("${src.getString("email")}\n${src.getString("phone")}")
                                    }
                                }
                            }
                        }

                        progress?.visibility = View.GONE
                        project_refresh?.isRefreshing = false
                    }
                }
                else -> {
                    Toast.makeText(context, "Произошла непредвиденная ошибка.", Toast.LENGTH_SHORT)
                        .show()

                    project_refresh?.isRefreshing = false
                }
            }
        }
    }
}