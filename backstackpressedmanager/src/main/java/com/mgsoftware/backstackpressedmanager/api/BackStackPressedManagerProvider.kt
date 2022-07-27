package com.mgsoftware.backstackpressedmanager.api

import com.mgsoftware.backstackpressedmanager.BackStackPressedManager

/**
 * Created by Mariusz
 */
interface BackStackPressedManagerProvider {
    val backStackPressedManager: BackStackPressedManager
}
