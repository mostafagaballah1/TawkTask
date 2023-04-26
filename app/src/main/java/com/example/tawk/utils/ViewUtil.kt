package com.example.tawk.utils

import android.app.Activity
import android.content.Context
import android.widget.*
import androidx.fragment.app.Fragment

fun Context?.toast(message: String?, timeout: Int = Toast.LENGTH_SHORT) {
    try {
        if (this != null)
            Toast.makeText(this, "$message", timeout).show()
    }catch (e:Exception){}
}

fun Fragment?.toast(message: String?, timeout: Int = Toast.LENGTH_SHORT) {
    try {
        if (this != null)
            Toast.makeText(context, "$message", timeout).show()
    }catch (e:Exception){}
}

fun Activity?.toast(message: String?, timeout: Int = Toast.LENGTH_SHORT) {
    try {
        if (this != null)
            Toast.makeText(this, "$message", timeout).show()
    }catch (e:Exception){}
}






