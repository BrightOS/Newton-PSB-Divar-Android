package ru.psbank.newton.ui.adaptation

import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import ru.psbank.newton.R
import ru.psbank.newton.util.KotlinHolder

@EpoxyModelClass(layout = R.layout.item_subtask)
abstract class SubtaskModel(val fragment: Fragment) : EpoxyModelWithHolder<SubtaskModel.Holder>() {

    @EpoxyAttribute
    lateinit var subTaskName: String

    @EpoxyAttribute
    var subTaskFinished = false

    @EpoxyAttribute
    lateinit var dayTasks: String

    override fun bind(holder: Holder) {
        holder.title.text = subTaskName
        holder.check.isChecked = subTaskFinished

        holder.check.setOnClickListener {
            holder.check.isChecked = subTaskFinished
            fragment.findNavController().navigate(
                AdaptationFragmentDirections.actionAdaptationFragmentToTasksFragment(dayTasks, subTaskName)
            )
        }

        holder.root.setOnClickListener {
            fragment.findNavController().navigate(
                AdaptationFragmentDirections.actionAdaptationFragmentToTasksFragment(dayTasks, subTaskName)
            )
        }
    }

    inner class Holder : KotlinHolder() {
        val root by bind<MaterialCardView>(R.id.card_root)
        val title by bind<TextView>(R.id.title)
        val check by bind<CheckBox>(R.id.check)
    }
}