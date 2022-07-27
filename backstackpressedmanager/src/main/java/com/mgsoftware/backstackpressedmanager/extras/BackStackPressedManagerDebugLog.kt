package com.mgsoftware.backstackpressedmanager.extras

import android.util.Log
import com.mgsoftware.backstackpressedmanager.BackStackPressedManager
import com.mgsoftware.backstackpressedmanager.api.BackStackChangedListener

class BackStackPressedManagerDebugLog(
    private val backStackPressedManager: BackStackPressedManager
) : BackStackChangedListener {

    override fun onBackStackChanged() {
        val sb = StringBuilder()
        sb.append(" \n\n")
        sb.append("---------- BackStackPressedManager  ----------").append('\n')
        for (index in 0 until backStackPressedManager.getBackStackEntryCount()) {
            val backStackEntry = backStackPressedManager.getBackStackEntryAt(index)
            sb.append(backStackEntry).append('\n')
        }
        sb.append("---------- BackStackPressedManager  ----------")
        sb.append("\n\n ")
        Log.d("BackStackPressedManager", sb.toString())
    }
}