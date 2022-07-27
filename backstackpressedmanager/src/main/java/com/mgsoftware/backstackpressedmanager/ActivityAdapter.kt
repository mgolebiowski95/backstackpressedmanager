package com.mgsoftware.backstackpressedmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mgsoftware.backstackpressedmanager.api.BackStackPressedManagerProvider
import com.mgsoftware.backstackpressedmanager.api.IdGenerator

open class ActivityAdapter: BackStackPressedManagerProvider {
    override lateinit var backStackPressedManager: BackStackPressedManager

    fun onCreate(activity: AppCompatActivity, savedInstanceState: Bundle?) {
        val fragmentProvider = DefaultFragmentProvider(activity.supportFragmentManager)
        val runnableProviderProvider = DefaultRunnableProviderProvider(fragmentProvider)
        val idGenerator: IdGenerator = AndroidViewIdGenerator()
        backStackPressedManager = BackStackPressedManager(runnableProviderProvider, idGenerator)
        if (savedInstanceState != null)
            backStackPressedManager.restoreInstanceState(savedInstanceState)
    }

    fun onSaveInstanceState(outState: Bundle) {
        backStackPressedManager.saveInstanceState(outState)
    }

    fun onBackPressed(): Boolean {
        return backStackPressedManager.popBackStack()
    }
}