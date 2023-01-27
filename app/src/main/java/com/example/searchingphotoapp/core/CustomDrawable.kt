package com.example.searchingphotoapp.core

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import com.example.searchingphotoapp.R

object CustomDrawable {

    fun setEditableBackground(context: Context): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.cornerRadius = 8f
        drawable.setColor(ContextCompat.getColor(context,R.color.content))
        drawable.setStroke(2, ContextCompat.getColor(context,R.color.button))
        return drawable
    }

    fun setButtonBackground(context: Context): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.cornerRadius = 8f
        drawable.setColor(ContextCompat.getColor(context,R.color.button))
        return drawable
    }
}