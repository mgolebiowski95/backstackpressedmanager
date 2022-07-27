package com.mgsoftware.backstackpressedmanager

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mgsoftware.backstackpressedmanager.api.FragmentProvider

/**
 * Created by Mariusz
 */
internal class DefaultFragmentProvider(private val fragmentManager: FragmentManager) :
    FragmentProvider {

    override fun getFragment(keyFragment: String): Fragment? {
        val fragments = mutableListOf<Fragment>()
        rFindFragment(fragmentManager, keyFragment, fragments)
        when {
            fragments.isEmpty() -> Log.w(TAG, "Fragment with tag '$keyFragment' not found.")
            containsDuplicates(fragments) -> Log.w(
                TAG,
                "In the fragment managers are fragments with the same tag. You should use different tag for fragments."
            )
        }
        return fragments.firstOrNull()
    }

    private fun rFindFragment(
        fm: FragmentManager,
        keyFragment: String,
        collector: MutableList<Fragment>
    ) {
        for (index in 0 until fm.fragments.size) {
            val fragment = fm.fragments[index]
            if (fragment.tag == keyFragment)
                collector.add(fragment)
            else
                rFindFragment(fragment.childFragmentManager, keyFragment, collector)
        }
    }

    private fun containsDuplicates(fragments: List<Fragment>): Boolean {
        return fragments.filter { it.tag != null }.groupBy { it.tag!! }.map { it.value.count() }
            .any { it > 1 }
    }

    companion object {
        private val TAG = DefaultFragmentProvider::class.java.simpleName
    }
}
