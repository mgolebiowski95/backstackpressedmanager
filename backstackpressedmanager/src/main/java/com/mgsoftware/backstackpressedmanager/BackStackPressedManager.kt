package com.mgsoftware.backstackpressedmanager

import android.os.Bundle
import com.mgsoftware.backstackpressedmanager.api.BackStackChangedListener
import com.mgsoftware.backstackpressedmanager.api.IdGenerator
import com.mgsoftware.backstackpressedmanager.api.RunnableProvider
import com.mgsoftware.backstackpressedmanager.api.RunnableProviderProvider
import java.io.Serializable
import java.util.*

/**
 * Created by Mariusz
 */
class BackStackPressedManager(
    private val runnableProviderProvider: RunnableProviderProvider,
    private val idGenerator: IdGenerator
) {

    data class BackStackEntry(val id: Int, val keyFragment: String, val keyRunnable: String) :
        Serializable

    private val stack = Stack<BackStackEntry>()
    private val backStackChangedListeners = mutableListOf<BackStackChangedListener>()

    fun popBackStack(): Boolean {
        if (!stack.isEmpty()) {
            val keyFragment = stack.peek().keyFragment
            val keyRunnable = stack.pop().keyRunnable

            val runnableProvider: RunnableProvider? =
                runnableProviderProvider.getRunnableProvider(keyFragment)
            return if (runnableProvider != null) {
                val runnable = runnableProvider.getRunnable(keyRunnable)
                val onBackPressed = runnable.onBackPressed()
                backStackChangedListeners.forEach { it.onBackStackChanged() }
                if (!onBackPressed)
                    popBackStack()
                else
                    true
            } else {
                false
            }
        } else {
            return false
        }
    }

    fun addToBackStack(keyFragment: String, keyRunnable: String) {
        val id = idGenerator.generateId()
        addToBackStack(id, keyFragment, keyRunnable)
    }

    fun addToBackStack(id: Int, keyFragment: String, keyRunnable: String) {
        val anyIsTheSame = stack.any {
            val sameIds = it.id == id
            val sameFragmentKeys = it.keyFragment == keyFragment
            val sameRunnableKeys = it.keyRunnable == keyRunnable
            sameIds && sameFragmentKeys && sameRunnableKeys
        }
        if (!anyIsTheSame) {
            stack.push(BackStackEntry(id, keyFragment, keyRunnable))
            backStackChangedListeners.forEach { it.onBackStackChanged() }
        }
    }

    fun removeByKeyFragment(keyFragment: String) {
        val lastSize = stack.size
        val iterator = stack.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.keyFragment == keyFragment)
                iterator.remove()
        }
        if (stack.size != lastSize)
            backStackChangedListeners.forEach { it.onBackStackChanged() }
    }

    fun restoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.containsKey(KEY)) {
            val serializable = savedInstanceState.getSerializable(KEY)
            if (serializable is List<*>) {
                @Suppress("UNCHECKED_CAST")
                val list = serializable as List<BackStackEntry>
                for (pair in list)
                    stack.push(pair)
            }
        }
        backStackChangedListeners.forEach { it.onBackStackChanged() }
    }

    fun saveInstanceState(outState: Bundle) {
        val list = ArrayList<BackStackEntry>()
        for (pair in stack)
            list.add(BackStackEntry(pair.id, pair.keyFragment, pair.keyRunnable))
        outState.putSerializable(KEY, list as Serializable)
    }

    fun getBackStackEntryCount(): Int {
        return stack.size
    }

    fun getBackStackEntryAt(index: Int): BackStackEntry {
        return stack[index]
    }

    fun addOnBackStackChangedListener(listener: BackStackChangedListener) {
        if (!backStackChangedListeners.contains(listener))
            backStackChangedListeners.add(listener)
    }

    fun removeOnBackStackChangedListener(listener: BackStackChangedListener) {
        if (backStackChangedListeners.contains(listener))
            backStackChangedListeners.remove(listener)
    }

    companion object {
        private val TAG = BackStackPressedManager::class.java.simpleName
        private val KEY = BackStackPressedManager::class.java.simpleName
    }
}
