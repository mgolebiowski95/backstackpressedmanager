package com.mgsoftware.backstackpressedmanager.api

/**
 * Created by Mariusz
 */
interface RunnableProvider {

    fun getRunnable(keyRunnable: String): OnBackPressedListener

    fun putRunnable(keyRunnable: String, runnable: OnBackPressedListener)
}
