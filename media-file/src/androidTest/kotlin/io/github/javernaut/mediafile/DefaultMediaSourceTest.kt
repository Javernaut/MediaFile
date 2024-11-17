package io.github.javernaut.mediafile

import androidx.test.platform.app.InstrumentationRegistry
import io.github.javernaut.mediafile.factory.MediaFileFactory
import org.junit.Before

abstract class DefaultMediaSourceTest {

    protected lateinit var factory: MediaFileFactory
        private set

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        factory = MediaFileFactory(context)
    }
}
