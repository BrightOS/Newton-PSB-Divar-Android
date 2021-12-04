package ru.psbank.newton.ui.adaptation.tasks

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.psbank.newton.App
import ru.psbank.newton.R
import ru.psbank.newton.data.PreferenceRepository
import ru.psbank.newton.util.KotlinHolder
import ru.psbank.newton.util.api.DoneTask

@EpoxyModelClass(layout = R.layout.item_task)
abstract class TaskModel(val fragment: Fragment) : EpoxyModelWithHolder<TaskModel.Holder>() {

    @EpoxyAttribute
    lateinit var title: String

    @EpoxyAttribute
    lateinit var description: String

    @EpoxyAttribute
    var checked = false

    @EpoxyAttribute
    var taskID = 0

    override fun bind(holder: Holder) {
        holder.title.text = title
        holder.description.text = description
        var tempChecked = checked
        var sending = false
        holder.check.isChecked = tempChecked

        val preferenceRepository = (fragment.requireActivity().application as App).preferenceRepository

        holder.check.setOnCheckedChangeListener { compoundButton, b ->
            if (tempChecked)
                holder.check.isChecked = true

            if (!sending && !checked && !tempChecked) {
                sending = true
                holder.check.alpha = 0.4f
                preferenceRepository.refreshMain = true

                holder.progress.visibility = View.VISIBLE
                GlobalScope.launch {
                    val response = DoneTask(
                        taskID,
                        preferenceRepository.token
                    ).get()

                    when (response.code) {
                        200 -> {
                            sending = false
                            MainScope().launch {
                                holder.progress.visibility = View.GONE
                                holder.check.alpha = 1f
                                tempChecked = true
                            }
                        }
                        else -> {
                            MainScope().launch {
                                Toast.makeText(
                                    fragment.context,
                                    "Произошла непредвиденная ошибка.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                sending = false
                                holder.check.isChecked = tempChecked
                                holder.check.alpha = 1f
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getViewType(): Int {
        return taskID
    }

    inner class Holder : KotlinHolder() {
        val title by bind<TextView>(R.id.title)
        val description by bind<TextView>(R.id.description)
        val check by bind<CheckBox>(R.id.check)
        val circle by bind<View>(R.id.circle)
        val progress by bind<ProgressBar>(R.id.loading)
    }
}