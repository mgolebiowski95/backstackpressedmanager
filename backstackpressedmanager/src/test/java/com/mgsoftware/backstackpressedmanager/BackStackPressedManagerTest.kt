package com.mgsoftware.backstackpressedmanager

import com.mgsoftware.backstackpressedmanager.api.IdGenerator
import com.mgsoftware.backstackpressedmanager.api.OnBackPressedListener
import com.mgsoftware.backstackpressedmanager.api.RunnableProvider
import com.mgsoftware.backstackpressedmanager.api.RunnableProviderProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import org.junit.Before
import org.junit.Test

class BackStackPressedManagerTest {
    private lateinit var SUT: BackStackPressedManager

    private val runnableProviderProvider: RunnableProviderProvider = mockk()
    private val idGenerator: IdGenerator = FakeIdGenerator()

    private val onBackStackPressedListener: OnBackPressedListener = mockk()
    private val runnableProvider: RunnableProvider = mockk()

    @Before
    fun setUp() {
        SUT = BackStackPressedManager(runnableProviderProvider, idGenerator)
    }

    //region addToBackStack
    @Test
    fun `can add one runnable to fragment`() {

        // arrange
        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_A)

        // act
        val result = SUT.getBackStackEntryCount()

        // assert
        result.shouldBeEqualTo(1)
    }

    @Test
    fun `#1 can add two the same runnables to one fragment with different ids`() {

        // arrange
        val keyFragment = FRAGMENT_A
        SUT.addToBackStack(keyFragment, RUNNABLE_A)
        SUT.addToBackStack(keyFragment, RUNNABLE_A)

        // act
        val result = SUT.getBackStackEntryCount()

        // assert
        result.shouldBeEqualTo(2)
    }

    @Test
    fun `#2 can add two the same runnables to one fragment with different ids`() {

        // arrange
        val keyFragment = FRAGMENT_A
        SUT.addToBackStack(ID_0, keyFragment, RUNNABLE_A)
        SUT.addToBackStack(ID_1, keyFragment, RUNNABLE_A)

        // act
        val result = SUT.getBackStackEntryCount()

        // assert
        result.shouldBeEqualTo(2)
    }

    @Test
    fun `can not add two the same runnables to one fragment with the same ids`() {

        // arrange
        val keyFragment = FRAGMENT_A
        SUT.addToBackStack(ID_0, keyFragment, RUNNABLE_A)

        // act
        SUT.addToBackStack(ID_0, keyFragment, RUNNABLE_A)

        // assert
        SUT.getBackStackEntryCount().shouldBeEqualTo(1)
    }

    @Test
    fun `can add two the same runnables to two fragments`() {

        // arrange
        var keyFragment = FRAGMENT_A
        val keyRunnable = RUNNABLE_A
        SUT.addToBackStack(keyFragment, keyRunnable)

        // act
        keyFragment = FRAGMENT_B
        SUT.addToBackStack(keyFragment, keyRunnable)

        // assert
        SUT.getBackStackEntryCount().shouldBeEqualTo(2)
    }

    @Test
    fun `can add two different runnables to one fragment`() {

        // arrange
        val keyFragment = FRAGMENT_A
        var keyRunnable = RUNNABLE_A
        SUT.addToBackStack(keyFragment, keyRunnable)

        // act
        keyRunnable = RUNNABLE_B
        SUT.addToBackStack(keyFragment, keyRunnable)

        // assert
        SUT.getBackStackEntryCount().shouldBeEqualTo(2)
    }
    //endregion

    //region popBackStack
    @Test
    fun `popBackStack with one runnable`() {

        // arrange
        every { onBackStackPressedListener.onBackPressed() } returns true
        every { runnableProvider.getRunnable(RUNNABLE_A) } returns onBackStackPressedListener
        every { runnableProviderProvider.getRunnableProvider(FRAGMENT_A) } returns runnableProvider

        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_A)

        // act
        val result = SUT.popBackStack()

        // assert
        verify { onBackStackPressedListener.onBackPressed() }
        result.shouldBeTrue()
        SUT.getBackStackEntryCount().shouldBeEqualTo(0)
    }

    @Test
    fun `popBackStack with two runnables should pop one runnables`() {

        // arrange
        every { onBackStackPressedListener.onBackPressed() } returns true
        every { runnableProvider.getRunnable(RUNNABLE_A) } returns onBackStackPressedListener
        every { runnableProvider.getRunnable(RUNNABLE_B) } returns onBackStackPressedListener
        every { runnableProviderProvider.getRunnableProvider(FRAGMENT_A) } returns runnableProvider

        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_A)
        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_B)

        // act
        SUT.popBackStack()

        // assert
        verify(exactly = 1) { onBackStackPressedListener.onBackPressed() }
        SUT.getBackStackEntryCount().shouldBeEqualTo(1)
    }

    @Test
    fun `popBackStack twice with two runnables should pop two runnables`() {

        // arrange
        every { onBackStackPressedListener.onBackPressed() } returns true
        every { runnableProvider.getRunnable(RUNNABLE_A) } returns onBackStackPressedListener
        every { runnableProvider.getRunnable(RUNNABLE_B) } returns onBackStackPressedListener
        every { runnableProviderProvider.getRunnableProvider(FRAGMENT_A) } returns runnableProvider

        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_A)
        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_B)

        // act
        SUT.popBackStack()
        SUT.popBackStack()

        // assert
        verify(exactly = 2) { onBackStackPressedListener.onBackPressed() }
        SUT.getBackStackEntryCount().shouldBeEqualTo(0)
    }

    @Test
    fun `popBackStack with two runnables (ignore, handle) should pop one runnables`() {

        // arrange
        every { runnableProvider.getRunnable(RUNNABLE_A) } returns FakeOnBackPressedListener(false)
        every { runnableProvider.getRunnable(RUNNABLE_B) } returns FakeOnBackPressedListener(true)
        every { runnableProviderProvider.getRunnableProvider(FRAGMENT_A) } returns runnableProvider

        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_A)
        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_B)

        // act
        SUT.popBackStack()

        // assert
        SUT.getBackStackEntryCount().shouldBeEqualTo(1)
    }

    @Test
    fun `popBackStack with two runnables (handle, ignore) should pop two runnables`() {

        // arrange
        every { runnableProvider.getRunnable(RUNNABLE_A) } returns FakeOnBackPressedListener(true)
        every { runnableProvider.getRunnable(RUNNABLE_B) } returns FakeOnBackPressedListener(false)
        every { runnableProviderProvider.getRunnableProvider(FRAGMENT_A) } returns runnableProvider

        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_A)
        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_B)

        // act
        SUT.popBackStack()

        // assert
        SUT.getBackStackEntryCount().shouldBeEqualTo(0)
    }

    @Test
    fun `popBackStack with runnables (handle, ignore, ignore) should pop three runnables`() {

        // arrange
        every { runnableProvider.getRunnable(RUNNABLE_A) } returns FakeOnBackPressedListener(true)
        every { runnableProvider.getRunnable(RUNNABLE_B) } returns FakeOnBackPressedListener(false)
        every { runnableProvider.getRunnable(RUNNABLE_C) } returns FakeOnBackPressedListener(false)
        every { runnableProviderProvider.getRunnableProvider(FRAGMENT_A) } returns runnableProvider

        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_A)
        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_B)
        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_C)

        // act
        SUT.popBackStack()

        // assert
        SUT.getBackStackEntryCount().shouldBeEqualTo(0)
    }

    @Test
    fun `popBackStack with runnables (ignore, handle, ignore) should pop two runnables`() {

        // arrange
        every { runnableProvider.getRunnable(RUNNABLE_A) } returns FakeOnBackPressedListener(false)
        every { runnableProvider.getRunnable(RUNNABLE_B) } returns FakeOnBackPressedListener(true)
        every { runnableProvider.getRunnable(RUNNABLE_C) } returns FakeOnBackPressedListener(false)
        every { runnableProviderProvider.getRunnableProvider(FRAGMENT_A) } returns runnableProvider

        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_A)
        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_B)
        SUT.addToBackStack(FRAGMENT_A, RUNNABLE_C)

        // act
        SUT.popBackStack()

        // assert
        SUT.getBackStackEntryCount().shouldBeEqualTo(1)
    }
    //endregion

    private class FakeOnBackPressedListener(private val handle: Boolean) : OnBackPressedListener {

        override fun onBackPressed(): Boolean {
            return handle
        }
    }

    companion object {
        private const val ID_0 = 0
        private const val ID_1 = 1
        private const val ID_2 = 2

        private const val FRAGMENT_A = "fragmentA"
        private const val FRAGMENT_B = "fragmentB"
        private const val FRAGMENT_C = "fragmentC"

        private const val RUNNABLE_A = "runnableA"
        private const val RUNNABLE_B = "runnableB"
        private const val RUNNABLE_C = "runnableC"
    }
}