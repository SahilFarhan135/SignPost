package com.example.signpost.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.signpost.base.BaseActivity
import com.google.android.material.snackbar.Snackbar

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