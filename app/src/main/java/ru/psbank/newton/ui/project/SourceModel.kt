package ru.psbank.newton.ui.project

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import ru.psbank.newton.R
import ru.psbank.newton.util.KotlinHolder
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


@EpoxyModelClass(layout = R.layout.item_source)
abstract class SourceModel(val fragment: Fragment) : EpoxyModelWithHolder<SourceModel.Holder>() {

    @EpoxyAttribute
    lateinit var url: String

    @EpoxyAttribute
    lateinit var title: String

    override fun bind(holder: Holder) {
        holder.title.setText(title)
        holder.cardRoot.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(fragment.requireContext(), browserIntent, null)
        }
    }

    inner class Holder : KotlinHolder() {
        val title by bind<TextView>(R.id.title)
        val cardRoot by bind<MaterialCardView>(R.id.card_root)
    }
}