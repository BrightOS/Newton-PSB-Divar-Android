package ru.psbank.newton.ui.project

import android.content.Intent
import android.net.Uri
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import ru.psbank.newton.R
import ru.psbank.newton.util.KotlinHolder

@EpoxyModelClass(layout = R.layout.item_goal)
abstract class GoalModel : EpoxyModelWithHolder<GoalModel.Holder>() {

    @EpoxyAttribute
    lateinit var subtitle: String

    @EpoxyAttribute
    lateinit var title: String

    override fun bind(holder: Holder) {
        holder.title.setText(title)
        holder.description.setText(subtitle)
    }

    inner class Holder : KotlinHolder() {
        val title by bind<TextView>(R.id.title)
        val description by bind<TextView>(R.id.description)
    }
}