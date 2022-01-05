package com.example.signpost.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.signpost.base.BaseActivity
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class UiUtil(private val context: Context) {

    private lateinit var snackbar: Snackbar
    private lateinit var progressDialog: ProgressDialog

    fun showMessage(
        message: String,
        length: Int = Snackbar.LENGTH_SHORT,
        button: Boolean = false,
        buttonText: String = "Ok"
    ) {
        showSnackBar(message, length, button, buttonText)
    }

    fun showToast(
        message: String,
        length: Int = Toast.LENGTH_SHORT
    ) {
        Toast.makeText(context, message, length)
            .show()
    }


    fun showProgress() {
        if (!::progressDialog.isInitialized) {
            progressDialog = ProgressDialog(context)
        }
        progressDialog.show()
    }


    fun hideProgress() {
        if (!::progressDialog.isInitialized) {
            progressDialog = ProgressDialog(context)
            return
        }
        progressDialog.dismiss()
    }


    private fun showSnackBar(
        message: String,
        snackBarLength: Int = Snackbar.LENGTH_LONG,
        button: Boolean = false,
        buttonText: String = "Ok"
    ) {
        if (!::snackbar.isInitialized) {
            (context as BaseActivity<*>).getLayoutBinding()
                .root.let {
                    snackbar = Snackbar.make(it, message, snackBarLength)
                }
        }
        snackbar.setText(message)
        snackbar.show()
    }

}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

@SuppressLint("SimpleDateFormat")
fun String.toDate(): Int {
    val c = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    try {
        c.time = sdf.parse(this)
    } catch (e: ParseException) {

    }
    return c[Calendar.DATE]
}

fun findDifference(
    start_date: String?,
    end_date: String?
): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    if (start_date.isNullOrEmpty() || end_date.isNullOrEmpty() || start_date.equals(
            "null",
            true
        ) || end_date.equals("null", true)
    ) {
        return "8"
    }
    val d1 = sdf.parse(start_date)
    val d2 = sdf.parse(end_date)
    val MILLI_TO_HOUR = 1000 * 60 * 60;
    return ((d2.time - d1.time) / MILLI_TO_HOUR).toString();
}

fun findNoOfDaysBetweenDates(firstDate: String, lastDate: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val d1 = sdf.parse(firstDate)
    val d2 = sdf.parse(lastDate)
    val milliToDay = (24 * 60 * 60 * 1000)
    val days = (d2.time - d1.time) / milliToDay + 1
    return days.toString()
}

fun String.isWeekend(): Boolean {
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val d1 = sdf.parse(this)
    cal.time = d1
    val day = cal[Calendar.DAY_OF_WEEK]
    return day == Calendar.SATURDAY || day == Calendar.SUNDAY
}

fun String.getMonthInString():String{
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val d1 = sdf.parse(this)
    cal.time = d1
    val month = SimpleDateFormat("MMMM").format(cal.time);
    return month
}