package com.mgsoftware.backstackpressedmanager.api

import com.mgsoftware.backstackpressedmanager.ActivityAdapter

interface ActivityAdapterProvider {

    fun getActivityAdapter(): ActivityAdapter
}