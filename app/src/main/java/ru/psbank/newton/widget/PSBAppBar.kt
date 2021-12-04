package ru.psbank.newton.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.text.Spannable
import android.text.SpannableString
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.appbar.AppBarLayout
import ru.psbank.newton.R
import ru.psbank.newton.util.getColorFromAttributes

class PSBAppBar constructor(
    cont: Context,
    attrs: AttributeSet?
) : AppBarLayout(cont, attrs) {

    fun setStartButtonOnClickListener(p0: (View) -> Unit) {
        findViewById<ImageView>(R.id.start_button).setOnClickListener(p0)
    }

    fun setEndButtonOnClickListener(p0: (View) -> Unit) {
        findViewById<ImageView>(R.id.end_button).setOnClickListener(p0)
    }

    fun hideEndButton() {
        findViewById<ImageView>(R.id.end_button).visibility = View.INVISIBLE
    }

    fun showEndButton() {
        findViewById<ImageView>(R.id.end_button).visibility = View.VISIBLE
    }

    fun setText(text: String) {
        findViewById<TextView>(R.id.appbar_title).text = text
    }

    val startButton: ImageView
        get() = findViewById(R.id.start_button)

    val endButton: ImageView
        get() = findViewById(R.id.end_button)

    init {
        View.inflate(context, R.layout.layout_psb_appbar, this)

        elevation = 0f
        background = ColorDrawable(getColorFromAttributes(cont, R.attr.colorPrimaryDark))

        addOnOffsetChangedListener(object : OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                this@PSBAppBar.findViewById<TextView>(R.id.appbar_title)
                    ?.setTextSize(
                        TypedValue.COMPLEX_UNIT_DIP,
                        28f + verticalOffset / (appBarLayout.totalScrollRange.toFloat() / 4)
                    )
            }
        })

        val text = findViewById<View>(R.id.appbar_title) as TextView
        text.isSelected = true

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.PSBAppBar)

            findViewById<ImageView>(R.id.start_button).setImageResource(
                typedArray.getResourceId(
                    R.styleable.PSBAppBar_startIcon,
                    R.drawable.ic_arrow_back
                )
            )

            typedArray.getResourceId(
                R.styleable.PSBAppBar_endIcon,
                0
            ).let {
                if (it != 0)
                    findViewById<ImageView>(R.id.end_button).setImageResource(it)
            }

            findViewById<TextView>(R.id.appbar_title).setText(
                typedArray.getText(
                    R.styleable.PSBAppBar_titleText
                )
            )

            typedArray.recycle()
        }
    }
}