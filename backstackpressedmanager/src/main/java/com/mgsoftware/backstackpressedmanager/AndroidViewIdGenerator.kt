package com.mgsoftware.backstackpressedmanager

import android.view.View
import com.mgsoftware.backstackpressedmanager.api.IdGenerator

class AndroidViewIdGenerator: IdGenerator {

    override fun generateId(): Int {
        return View.generateViewId()
    }
}