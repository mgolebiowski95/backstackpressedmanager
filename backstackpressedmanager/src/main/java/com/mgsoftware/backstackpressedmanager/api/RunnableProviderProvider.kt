package com.mgsoftware.backstackpressedmanager.api

interface RunnableProviderProvider {

    fun getRunnableProvider(keyFragment: String): RunnableProvider?
}