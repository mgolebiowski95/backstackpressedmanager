package com.mgsoftware.backstackpressedmanager

import com.mgsoftware.backstackpressedmanager.api.IdGenerator

class FakeIdGenerator : IdGenerator {
    private var count = -1

    override fun generateId(): Int {
        return count++
    }
}