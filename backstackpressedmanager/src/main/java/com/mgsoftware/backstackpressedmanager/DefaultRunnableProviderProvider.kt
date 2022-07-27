package com.mgsoftware.backstackpressedmanager

import com.mgsoftware.backstackpressedmanager.api.FragmentAdapterProvider
import com.mgsoftware.backstackpressedmanager.api.FragmentProvider
import com.mgsoftware.backstackpressedmanager.api.RunnableProvider
import com.mgsoftware.backstackpressedmanager.api.RunnableProviderProvider

class DefaultRunnableProviderProvider(
    private val fragmentProvider: FragmentProvider
): RunnableProviderProvider {

    override fun getRunnableProvider(keyFragment: String): RunnableProvider? {
        return when (val fragment = fragmentProvider.getFragment(keyFragment)) {
            is RunnableProvider -> fragment
            is FragmentAdapterProvider -> fragment.fragmentAdapter
            else -> null
        }
    }
}