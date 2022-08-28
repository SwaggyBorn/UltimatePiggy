package com.bornproduct.ultimatepiggy.expand

import android.graphics.Paint.Style.STROKE
import android.graphics.Typeface
import android.widget.TextView
import org.w3c.dom.Text

fun TextView.setThemeBigTypeFaceStoke(){
  typeface = Typeface.createFromAsset(context.assets,"font/theme_big_size_text.ttf")
  paint.style = STROKE
  paint.strokeWidth = 5f
}