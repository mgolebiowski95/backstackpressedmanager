package com.mgsoftware.backstackpressedmanager

import androidx.fragment.app.Fragment
import com.mgsoftware.backstackpressedmanager.api.ActivityAdapterProvider
import com.mgsoftware.backstackpressedmanager.api.BackStackPressedManagerProvider
import com.mgsoftware.backstackpressedmanager.api.OnBackPressedListener
import com.mgsoftware.backstackpressedmanager.api.RunnableProvider
import java.util.*

class FragmentAdapter(private val fragment: Fragment) : RunnableProvider {
    private val runnableMap = HashMap<String, OnBackPressedListener>()

    private lateinit var backStackPressedManagerProvider: BackStackPressedManagerProvider
    val backStackPressedManager: BackStackPressedManager
        get() = backStackPressedManagerProvider.backStackPressedManager

    fun onAttach() {
        if (fragment.requireActivity() is BackStackPressedManagerProvider)
            backStackPressedManagerProvider =
                fragment.requireActivity() as BackStackPressedManagerProvider
        else if (fragment.requireActivity() is ActivityAdapterProvider)
            backStackPressedManagerProvider =
                (fragment.requireActivity() as ActivityAdapterProvider).getActivityAdapter()
    }

    fun onDetach() {
        removeAssociatedRunnable(fragment.tag)
    }

    override fun getRunnable(keyRunnable: String): OnBackPressedListener {
        return runnableMap.getValue(keyRunnable)
    }

    override fun putRunnable(keyRunnable: String, runnable: OnBackPressedListener) {
        runnableMap[keyRunnable] = runnable
    }

    private fun removeAssociatedRunnable(keyFragment: String?) {
        keyFragment?.let {
            backStackPressedManager.removeByKeyFragment(it)
        }
    }
}