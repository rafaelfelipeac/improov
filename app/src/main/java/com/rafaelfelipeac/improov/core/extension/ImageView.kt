package com.rafaelfelipeac.improov.core.extension

import android.content.Context
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.rafaelfelipeac.improov.features.commons.Goal
import kotlin.math.ceil

fun ImageView.enableIcon(iconNormal: Int, context: Context) {
    background = ContextCompat.getDrawable(context, iconNormal)
}

fun ImageView.disableIcon(iconDark: Int, context: Context) {
    background = ContextCompat.getDrawable(context, iconDark)
}

fun ImageView.setWidthForProgress(goal: Goal, width: Int) {
    var px = if (goal.divideAndConquer) {
        val widthUnit = width / goal.goldValue
        ceil((goal.value * widthUnit)).toInt()
    } else {
        val widthUnit = width / goal.singleValue
        ceil((goal.value * widthUnit)).toInt()
    }

    val lp = layoutParams as ConstraintLayout.LayoutParams

    if (px == 0) { // if px was 0, the width will be equivalent a match_constraint
        px = 1
    }
    if (px > width) { // if px was greater than width, need to limit
        px = width
    }

    lp.width = px
    layoutParams = lp
}
