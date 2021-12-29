package com.example.signpost.base

import android.app.Activity
import android.app.ActivityOptions
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat

class Navigator(var context: AppCompatActivity) {

    fun startActivity(
        activityClass: Class<out Activity>,
        noTransitionAnimation: Boolean = false
    ) {
        val (activity, intent) = getActivityIntent(activityClass)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || noTransitionAnimation) {
            activity.startActivity(intent)
        } else {
            activity.startActivity(
                intent, ActivityOptions.makeSceneTransitionAnimation(activity)
                    .toBundle()
            )
        }
    }


    fun startActivityClearStack(activityClass: Class<out Activity>) {
        val (activity, intent) = getActivityIntent(activityClass)
        intent.flags =
            (Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP)
        activity.startActivity(intent)
        activity.finishAffinity()
    }

    fun finishActivity() {
        context.finish()
    }

    fun startActivityClearCurrent(
        activityClass: Class<out Activity>,
        noTransitionAnimation: Boolean = false
    ) {
        val (activity, intent) = getActivityIntent(activityClass)
        intent.flags =
            (Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || noTransitionAnimation) {
            activity.startActivity(intent)
        } else {
            activity.startActivity(
                intent, ActivityOptions.makeSceneTransitionAnimation(activity)
                    .toBundle()
            )
        }
        activity.finish()
    }




    private fun getActivityIntent(activityClass: Class<out Activity>): Pair<AppCompatActivity, Intent> {
        val activity = context
        val intent = Intent(activity, activityClass)
        return Pair(activity, intent)
    }

    fun addFragmentWithData(
        containerId: Int,
        fragment: androidx.fragment.app.Fragment,
        fragmentToHide: androidx.fragment.app.Fragment,
        bundle: Bundle
    ) {
        fragment.arguments = bundle
        addFragment(containerId, fragment, fragmentToHide)
    }

    fun addFragment(
        containerId: Int,
        fragment: androidx.fragment.app.Fragment,
        fragmentToHide: androidx.fragment.app.Fragment
    ) {
        context.supportFragmentManager.beginTransaction()
            .add(containerId, fragment)
            .addToBackStack(fragment::class.java.simpleName)
            .hide(fragmentToHide)
            .commit()
    }

    fun replaceFragmentWithData(
        containerId: Int,
        fragment: androidx.fragment.app.Fragment,
        bundle: Bundle,
        addToBackStack: Boolean = false
    ) {
        fragment.arguments = bundle
        replaceFragment(containerId, fragment, addToBackStack)
    }

    fun replaceFragment(
        containerId: Int,
        fragment: androidx.fragment.app.Fragment,
        addToBackStack: Boolean = false
    ) {
        context.supportFragmentManager.beginTransaction()
            .replace(containerId, fragment)
            .apply { if (addToBackStack) addToBackStack(fragment::class.java.simpleName) }
            .commit()
    }

    fun goBack() {
        val fm = context.supportFragmentManager
        if (fm.backStackEntryCount > 0)
            fm.popBackStack()
        else
            context.onBackPressed()
    }

    fun canGoBack(): Boolean {
        val fm = context.supportFragmentManager
        return (fm.backStackEntryCount > 0)
    }

    fun popFragmentClearStack(tag: String = "") {
        context.supportFragmentManager.popBackStack(
            if (tag.isEmpty()) null else tag,
            androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }
    //Add more methods here... like add with animation

    fun addFragmentWithAnim(
        containerId: Int,
        fragment: androidx.fragment.app.Fragment,
        fragmentToHide: androidx.fragment.app.Fragment
    ) {
        context.supportFragmentManager.beginTransaction()
            .setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .add(containerId, fragment)
            .addToBackStack(fragment::class.java.simpleName)
            .hide(fragmentToHide)
            .commit()
    }

    fun replaceFragmentWithAnim(
        containerId: Int,
        fragment: androidx.fragment.app.Fragment,
        enterAnimId: Int,
        exitAnimId: Int,
        addToBackStack: Boolean = false
    ) {
        val transaction = context.supportFragmentManager.beginTransaction()
            .setCustomAnimations(enterAnimId, 0, 0, exitAnimId)
            .replace(containerId, fragment)
        if (addToBackStack) transaction.addToBackStack(fragment::class.java.simpleName)
        transaction.commit()

    }

    fun goToPlayStorePage() {
        val appPackageName = context.packageName
        try {
            val intentRateApp =
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
            context.startActivity(intentRateApp)
        } catch (e: ActivityNotFoundException) {
            val intentRateApp =
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(
                        "https://play.google.com/store/" +
                                "apps/details?id=" + appPackageName
                    )
                )
            context.startActivity(intentRateApp)
        }
    }

    fun openUrlInBrowser(url: String) {
        val i = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(url)
        }
        context.startActivity(i)
    }

}