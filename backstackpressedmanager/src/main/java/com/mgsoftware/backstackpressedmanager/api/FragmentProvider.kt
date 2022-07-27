package com.mgsoftware.backstackpressedmanager.api

import androidx.fragment.app.Fragment

/**
 * Created by Mariusz
 */
interface FragmentProvider {

    fun getFragment(keyFragment: String): Fragment?
}
